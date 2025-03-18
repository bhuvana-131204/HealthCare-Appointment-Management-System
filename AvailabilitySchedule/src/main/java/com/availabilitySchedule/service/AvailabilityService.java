package com.availabilitySchedule.service;

import com.availabilitySchedule.dto.AvailabilityDTO;
import com.availabilitySchedule.dto.DoctorDTO;
import com.availabilitySchedule.exception.NoAvailabilityFoundException;
import com.availabilitySchedule.exception.UnavailableException;
import com.availabilitySchedule.exception.DatabaseException;
import com.availabilitySchedule.model.Availability;
import com.availabilitySchedule.model.Specialization;
import com.availabilitySchedule.model.Status;
import com.availabilitySchedule.model.Timeslots;
import com.availabilitySchedule.repository.AvailabilityRepository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AvailabilityService {

	private final AvailabilityRepository availabilityRepository;

	//@Autowired
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

	public void updateAvailabilityStatus(String availableId, String unavailableId) {
	    Availability availableEntity = availabilityRepository.findById(availableId)
	            .orElseThrow(() -> new NoAvailabilityFoundException("Availability not found for ID: " + availableId));
	    Availability unavailableEntity = availabilityRepository.findById(unavailableId)
	            .orElseThrow(() -> new NoAvailabilityFoundException("Availability not found for ID: " + unavailableId));

	    availableEntity.setStatus(Status.Available);
	    unavailableEntity.setStatus(Status.Unavailable);

	    availabilityRepository.save(availableEntity);
	    availabilityRepository.save(unavailableEntity);
	}

	public List<Availability> getAvailabilityByDoctorIdAndDate(String doctorId, LocalDate date)
	{
	    List<Availability> availabilities = availabilityRepository.findByDoctorIdAndDate(doctorId, date);
	    if (availabilities == null || availabilities.isEmpty()) {
	        log.error("No availability found for doctorId and date: {} {}", doctorId, date);
	        throw new NoAvailabilityFoundException("No availability found for the specified doctor and date");
	    }
	    List<Availability> availableAvailabilities = availabilities.stream()
	            .filter(availability -> availability.getStatus() == Status.Available)
	            .collect(Collectors.toList());
	    if (availableAvailabilities.isEmpty()) {
	        log.error("No available slots found for doctorId and date: {} {}", doctorId, date);
	        throw new NoAvailabilityFoundException("No available slots found for the specified doctor and date");
	    }
	    return availableAvailabilities;
	}

	public List<Availability> getAvailabilityBySpecializationAndDate(Specialization specialization, LocalDate date)
	{
	    List<Availability> availabilities = availabilityRepository.findBySpecializationAndDate(specialization, date);
	    if (availabilities == null || availabilities.isEmpty()) {
	        log.error("No availability found for specialization and date: {} {}", specialization, date);
	        throw new UnavailableException("No availability found for the specified specialization and date");
	    }
	    List<Availability> availableAvailabilities = availabilities.stream()
	            .filter(availability -> availability.getStatus() == Status.Available)
	            .collect(Collectors.toList());
	    if (availableAvailabilities.isEmpty()) {
	        log.error("No available slots found for specialization and date: {} {}", specialization, date);
	        throw new UnavailableException("No available slots found for the specified specialization and date");
	    }
	    return availableAvailabilities;
	}

	public void blockTimeSlot(String availabilityId) throws NoAvailabilityFoundException, DatabaseException {
	    Availability availability = availabilityRepository.findById(availabilityId)
	            .orElseThrow(() -> new NoAvailabilityFoundException("Availability not found for ID: " + availabilityId));

	    if (availability.getStatus() != Status.Available) {
	        log.error("Time slot with ID: {} is not available", availabilityId);
	        throw new UnavailableException("Time slot is not available for blocking");
	    }

	    availability.setStatus(Status.Unavailable);

	    availabilityRepository.save(availability);
	}

	public List<Availability> viewAllAvailabilities() {
		return availabilityRepository.findAll();
	}

	public void deleteAvailability(String availabilityId) throws NoAvailabilityFoundException, DatabaseException {
		availabilityRepository.deleteById(availabilityId);
	}

}