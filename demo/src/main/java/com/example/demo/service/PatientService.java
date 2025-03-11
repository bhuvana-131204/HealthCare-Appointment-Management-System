package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.DoctorDto;
import com.example.demo.DTO.PatientDto;
import com.example.demo.enums.Specialization;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.NoDoctorFoundException;
import com.example.demo.exceptions.PhoneNumberAlreadyExistsException;
import com.example.demo.model.Doctor;
//import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.repository.PatientRepository;

@Service
public class PatientService {
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DoctorService doctorService;
	
	public PatientDto getPatientById(String id){
		Optional<Patient> patient = patientRepository.findBypatientId(id);
		Optional<User> user = userService.getUserById(id);
		PatientDto patientDto = new PatientDto();
		
		patientDto.setName(patient.get().getName());
		patientDto.setAge(patient.get().getAge());
		patientDto.setAddress(patient.get().getAddress());
		patientDto.setPatient_id(patient.get().getPatientId());
		patientDto.setEmail(user.get().getEmail());
		patientDto.setPassword(user.get().getPassword());
		patientDto.setGender(patient.get().getGender());
		patientDto.setPhoneNumber(user.get().getPhoneNumber());
			return patientDto;
	}
	
	public List<Doctor> getDoctorBySpecialization(Specialization specialization){
		List<Doctor> doctors = doctorService.getDoctorBySpecialization(specialization);
		if(doctors.isEmpty()) {
			throw new NoDoctorFoundException("No Doctor Found");
		}
		return doctors;
	}
	
	public void updatePatientDetails(PatientDto patUpdate) {
		Patient patient = patientRepository.findBypatientId(patUpdate.getPatient_id()).get();
		User user = userService.getUser(patUpdate.getPatient_id());
		if(patUpdate.getName()!=null) {
			patient.setName(patUpdate.getName());
		}
		if(patUpdate.getGender()!=null) {
			patient.setGender(patUpdate.getGender());
		}
		if(patUpdate.getAge() != null) {
			patient.setAge(patUpdate.getAge());
		}
		if(patUpdate.getAddress() != null) {
			patient.setAddress(patUpdate.getAddress());
		}
		if(patUpdate.getEmail()!=null) {
			if(userService.emailExist(patUpdate.getEmail())) {
				throw new EmailAlreadyExistsException("Email "+patUpdate.getEmail() +" given for Update Already Exist");
			}
			user.setEmail(patUpdate.getEmail());
		}
		if(patUpdate.getPhoneNumber()!=null) {
			if(userService.phoneNumberExist(patUpdate.getPhoneNumber())) {
				throw new PhoneNumberAlreadyExistsException("Phone Number "+ patUpdate.getPhoneNumber() +" given for Update Already Exist");
			}
			user.setPhoneNumber(patUpdate.getPhoneNumber());
		}
		if(patUpdate.getPassword()!=null) {
			user.setPassword(patUpdate.getPassword());
		}
		patientRepository.save(patient);
		userService.updateuser(user);
	}

}
