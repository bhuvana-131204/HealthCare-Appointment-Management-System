package com.availabilitySchedule.service;

import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.dto.DoctorDTO;
import com.availabilitySchedule.exception.AvailabilityNotFoundException;
import com.availabilitySchedule.exception.DoctorNotFoundException;
import com.availabilitySchedule.exception.UnavailableException;
import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Specialization;
import com.availabilitySchedule.model.Status;
import com.availabilitySchedule.model.Timeslots;
import com.availabilitySchedule.repository.AvailabilityRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing availability.
 * 
 * Provides methods to initialize, reset, update, and fetch availability.
 * 
 * @author Swapnil Rajesh
 * @since 18/02/2025
 */
@Service
@Slf4j
public class AvailabilityService {

	private final AvailabilityRepository availabilityRepository;

	public AvailabilityService(AvailabilityRepository availabilityRepository) {
		this.availabilityRepository = availabilityRepository;
	}

	/**
	 * Initializes availability if the repository is empty.
	 */
	@PostConstruct
	public void initializeAvailability() {
		if (availabilityRepository.count() == 0) {
			log.info("Initializing availability for the first time.");
			updateAvailabilityForWeek();
		}
	}

	/**
	 * Scheduled task to check and reset availability at midnight every day.
	 */
	@Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
	public void checkAndResetAvailability() {
		LocalDate currentDate = LocalDate.now();
		DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

		// Check if it's the start of a new week (Monday)
		if (dayOfWeek == DayOfWeek.MONDAY) {
			log.info("Resetting availability for the new week.");
			resetAvailability();
		}
	}

	/**
	 * Marks current week's availabilities as unavailable and updates availability
	 * for the new week.
	 */
	private void resetAvailability() {
		log.info("Marking current week's availabilities as unavailable.");
		List<Availability> currentWeekAvailabilities = availabilityRepository.findAll();
		for (Availability availability : currentWeekAvailabilities) {
			availability.setStatus(Status.Unavailable);
			availabilityRepository.save(availability);
		}

		log.info("Updating availability for the new week.");
		updateAvailabilityForWeek();
	}

	/**
	 * Updates availability for the upcoming week.
	 */
	private void updateAvailabilityForWeek() {
		List<DoctorDTO> doctors = List.of(new DoctorDTO("1", "Doctor A", Specialization.Cardiology),
				new DoctorDTO("2", "Doctor B", Specialization.Nephrology),
				new DoctorDTO("3", "Doctor C", Specialization.General));

		LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

		for (DoctorDTO doctor : doctors) {
			for (int i = 0; i < 5; i++) {
				LocalDate date = nextMonday.plusDays(i);

				for (Timeslots timeslot : Timeslots.values()) {
					AvailabilityDTO dto = new AvailabilityDTO(doctor.getId(), doctor.getSpecialization(), date,
							timeslot);
					Availability availability = dto.toEntity();
					availabilityRepository.save(availability);
				}
			}
		}
		log.info("Availability updated for the week starting from {}", nextMonday);
	}

	/**
	 * Updates past dates to unavailable.
	 */
	@PostConstruct
	public void updatePastDates() {
		log.info("Updating past dates to unavailable.");
		LocalDate currentDate = LocalDate.now();
		List<Availability> pastAvailabilities = availabilityRepository.findByDateBefore(currentDate);

		for (Availability availability : pastAvailabilities) {
			availability.setStatus(Status.Unavailable);
			availabilityRepository.save(availability);
		}
	}

	/**
	 * Updates the status of availability entities.
	 * 
	 * @param availableId   the ID of the availability to be marked as available
	 * @param unavailableId the ID of the availability to be marked as unavailable
	 */
	public void updateAvailabilityStatus(String availableId, String unavailableId) {
		log.info("Updating availability status for IDs: availableId={}, unavailableId={}", availableId, unavailableId);
		Availability availableEntity = availabilityRepository.findById(availableId)
				.orElseThrow(() -> new AvailabilityNotFoundException("Availability not found for ID: " + availableId));
		Availability unavailableEntity = availabilityRepository.findById(unavailableId).orElseThrow(
				() -> new AvailabilityNotFoundException("Availability not found for ID: " + unavailableId));

		availableEntity.setStatus(Status.Available);
		unavailableEntity.setStatus(Status.Unavailable);

		availabilityRepository.save(availableEntity);
		availabilityRepository.save(unavailableEntity);
	}

	/**
	 * Fetches availability by doctor ID and date.
	 * 
	 * @param doctorId the ID of the doctor
	 * @param date     the date of the availability
	 * @return a list of available availabilities for the specified doctor and date
	 */
	public List<Availability> getAvailabilityByDoctorIdAndDate(String doctorId, LocalDate date) {
		log.info("Fetching availability for doctorId={} and date={}", doctorId, date);
		if (availabilityRepository.findByDoctorId(doctorId) == null) {
			log.error("No Doctor found for doctorId: {}", doctorId);
			throw new DoctorNotFoundException("No Doctor found for doctorId: " + doctorId);
		}
		List<Availability> availabilities = availabilityRepository.findByDoctorIdAndDate(doctorId, date);
		if (availabilities == null || availabilities.isEmpty()) {
			log.error("No availability found for doctorId={} and date={}", doctorId, date);
			throw new AvailabilityNotFoundException("No availability found for the specified doctor and date");
		}
		List<Availability> availableAvailabilities = availabilities.stream()
				.filter(availability -> availability.getStatus() == Status.Available).collect(Collectors.toList());
		if (availableAvailabilities.isEmpty()) {
			log.error("No available slots found for doctorId={} and date={}", doctorId, date);
			throw new AvailabilityNotFoundException("No available slots found for the specified doctor and date");
		}
		return availableAvailabilities;
	}

	/**
	 * Fetches availability by specialization and date.
	 * 
	 * @param specialization the specialization of the doctor
	 * @param date           the date of the availability
	 * @return a list of available availabilities for the specified specialization
	 *         and date
	 */
	public List<Availability> getAvailabilityBySpecializationAndDate(Specialization specialization, LocalDate date) {
		log.info("Fetching availability for specialization={} and date={}", specialization, date);
		List<Availability> availabilities = availabilityRepository.findBySpecializationAndDate(specialization, date);
		if (availabilities == null || availabilities.isEmpty()) {
			log.error("No availability found for specialization={} and date={}", specialization, date);
			throw new UnavailableException("No availability found for the specified specialization and date");
		}
		List<Availability> availableAvailabilities = availabilities.stream()
				.filter(availability -> availability.getStatus() == Status.Available).collect(Collectors.toList());
		if (availableAvailabilities.isEmpty()) {
			log.error("No available slots found for specialization={} and date={}", specialization, date);
			throw new UnavailableException("No available slots found for the specified specialization and date");
		}
		return availableAvailabilities;
	}

	/**
	 * Fetches availability by doctor ID and date range.
	 * 
	 * @param doctorId  the ID of the doctor
	 * @param startDate the start date of the range
	 * @param endDate   the end date of the range
	 * @return a list of availabilities for the specified doctor and date range
	 */
	public List<Availability> getAvailabilityByDoctorIdAndDateRange(String doctorId, LocalDate startDate,
			LocalDate endDate) {
		log.info("Fetching availability for doctorId={}, startDate={}, endDate={}", doctorId, startDate, endDate);
		List<Availability> availabilities = availabilityRepository.findByDoctorIdAndDateBetween(doctorId, startDate,
				endDate);
		if (availabilities == null || availabilities.isEmpty()) {
			log.error("No availability found for doctorId={}, startDate={}, endDate={}", doctorId, startDate, endDate);
			throw new AvailabilityNotFoundException("No availability found for the specified doctor and date range");
		}
		return availabilities;
	}

	/**
	 * Fetches availability by specialization and date range.
	 * 
	 * @param specialization the specialization of the doctor
	 * @param startDate      the start date of the range
	 * @param endDate        the end date of the range
	 * @return a list of availabilities for the specified specialization and date
	 *         range
	 */
	public List<Availability> getAvailabilityBySpecializationAndDateRange(Specialization specialization,
			LocalDate startDate, LocalDate endDate) {
		log.info("Fetching availability for specialization={}, startDate={}, endDate={}", specialization, startDate,
				endDate);
		List<Availability> availabilities = availabilityRepository.findBySpecializationAndDateBetween(specialization,
				startDate, endDate);
		if (availabilities == null || availabilities.isEmpty()) {
			log.error("No availability found for specialization={}, startDate={}, endDate={}", specialization,
					startDate, endDate);
			throw new AvailabilityNotFoundException(
					"No availability found for the specified specialization and date range");
		}
		return availabilities;
	}

	/**
	 * Blocks a time slot by setting its status to unavailable.
	 * 
	 * @param availabilityId the ID of the availability to be blocked
	 */
	public void blockTimeSlot(String availabilityId) {
		log.info("Blocking time slot with ID: {}", availabilityId);
		Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
				() -> new AvailabilityNotFoundException("Availability not found for ID: " + availabilityId));

		if (availability.getStatus() != Status.Available) {
			log.error("Time slot with ID: {} is not available", availabilityId);
			throw new UnavailableException("Time slot is not available for blocking");
		}

		availability.setStatus(Status.Unavailable);
		availabilityRepository.save(availability);
	}

	/**
	 * Fetches all availabilities.
	 * 
	 * @return a list of all availabilities
	 */
	public List<Availability> viewAllAvailabilities() {
		log.info("Fetching all availabilities.");
		return availabilityRepository.findAll();
	}

	/**
	 * Deletes an availability by ID.
	 * 
	 * @param availabilityId the ID of the availability to be deleted
	 */
	public void deleteAvailability(String availabilityId) {
		log.info("Deleting availability with ID: {}", availabilityId);
		availabilityRepository.deleteById(availabilityId);
	}

	/**
	 * Views an availability by ID.
	 * 
	 * @param availabilityId the ID of the availability to be viewed
	 * @return the availability with the specified ID
	 */
	public Availability viewById(String availabilityId) {
		log.info("Viewing availability with ID: {}", availabilityId);
		Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
				() -> new AvailabilityNotFoundException("Availability not found for ID: " + availabilityId));

		if (availability.getStatus() != Status.Available) {
			log.error("Time slot with ID: {} is not available", availabilityId);
			throw new UnavailableException("Time slot is not available for blocking");
		}
		return availability;
	}

	/**
	 * Releases an availability by setting its status to available.
	 * 
	 * @param availabilityId the ID of the availability to be released
	 */
	public void releaseAvailabilityById(String availabilityId) {
		log.info("Releasing availability with ID: {}", availabilityId);
		Availability availability = availabilityRepository.findById(availabilityId).orElseThrow(
				() -> new AvailabilityNotFoundException("Availability not found for ID: " + availabilityId));

		if (availability.getStatus() == Status.Unavailable) {
			availability.setStatus(Status.Available);
		} else {
			log.error("Time slot with ID: {} is not unavailable", availabilityId);
			throw new UnavailableException("Time slot is not available for releasing");
		}
	}
}