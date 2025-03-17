package com.availabilitySchedule.service;

import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.exception.NoAvailabilityFoundException;
import com.availabilitySchedule.exception.DatabaseException;
import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.DoctorDTO;
import com.availabilitySchedule.model.Specialization;
import com.availabilitySchedule.model.Timeslots;
import com.availabilitySchedule.repository.AvailabilityRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AvailabilityService {

	private final AvailabilityRepository availabilityRepository;

	@Autowired
	public AvailabilityService(AvailabilityRepository availabilityRepository) {
		this.availabilityRepository = availabilityRepository;
	}

	@PostConstruct
	public void initializeAvailability() {
		if (availabilityRepository.count() == 0) {
			List<DoctorDTO> doctors = List.of(new DoctorDTO("1", "Doctor A", Specialization.Cardiology),
					new DoctorDTO("2", "Doctor B", Specialization.Nephrology),
					new DoctorDTO("3", "Doctor C", Specialization.General));

			for (DoctorDTO doctor : doctors) {
				for (int i = 1; i <= 5; i++) {
					LocalDate date = LocalDate.now().plusDays(i);

					for (Timeslots timeslot : Timeslots.values()) {
						AvailabilityDTO dto = new AvailabilityDTO(doctor.getId(), doctor.getName(),
								doctor.getSpecialization(), date, timeslot);
						Availability availability = dto.toEntity();
						availabilityRepository.save(availability);
					}
				}
			}
		}
	}

	@Scheduled(cron = "0 0 0 * * SAT,SUN") // Runs at midnight on Saturdays and Sundays
	public void checkAndResetAvailability() {
		LocalDate currentDate = LocalDate.now();
		DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

		if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
			resetAvailability();
		}
	}

	private void resetAvailability() {
		availabilityRepository.deleteAll();

		List<DoctorDTO> doctors = List.of(new DoctorDTO("1", "Doctor A", Specialization.Cardiology),
				new DoctorDTO("2", "Doctor B", Specialization.Nephrology),
				new DoctorDTO("3", "Doctor C", Specialization.General));

		for (DoctorDTO doctor : doctors) {
			for (int i = 1; i <= 5; i++) {
				LocalDate date = LocalDate.now().plusDays(i);

				for (Timeslots timeslot : Timeslots.values()) {
					AvailabilityDTO dto = new AvailabilityDTO(doctor.getId(), doctor.getName(),
							doctor.getSpecialization(), date, timeslot);
					Availability availability = dto.toEntity();
					availabilityRepository.save(availability);
				}
			}
		}
	}

	public void updateAvailability(AvailabilityDTO availabilityDTO) throws DatabaseException {
		Availability availability = availabilityDTO.toEntity();
		availability.setTimeSlots(availabilityDTO.getTimeSlots());
		availabilityRepository.save(availability);
		log.info("Updated availability for doctorId: {}", availability.getDoctorId());
	}

	public List<Availability> getAvailabilityByDoctorIdAndDate(String doctorId, LocalDate date)
			throws NoAvailabilityFoundException, DatabaseException {
		List<Availability> availabilities = availabilityRepository.findByDoctorIdAndDate(doctorId, date);
		if (availabilities == null) {
			log.error("No availability found for doctorId and date: {} {}", doctorId, date);
			throw new NoAvailabilityFoundException("No availability found for the specified doctor and date");
		}
		log.info("Fetched availability for doctorId and date: {} {}", doctorId, date);
		return availabilities;
	}

	public List<Availability> getAvailabilityBySpecializationAndDate(Specialization specialization, LocalDate date)
			throws NoAvailabilityFoundException, DatabaseException {
		List<Availability> availabilities = availabilityRepository.findBySpecializationAndDate(specialization, date);
		if (availabilities == null) {
			log.error("No availability found for specialization and date: {} {}", specialization, date);
			throw new NoAvailabilityFoundException("No availability found for the specified specialization and date");
		}
		log.info("Fetched availability for specialization and date: {} {}", specialization, date);
		return availabilities;
	}

	public void blockTimeSlot(String availabilityId) throws NoAvailabilityFoundException, DatabaseException {
		availabilityRepository.deleteById(availabilityId);
	}

	public List<Availability> viewAllAvailabilities() {
		return availabilityRepository.findAll();
	}

	public void deleteAvailability(String availabilityId) throws NoAvailabilityFoundException, DatabaseException {
		availabilityRepository.deleteById(availabilityId);
	}

}