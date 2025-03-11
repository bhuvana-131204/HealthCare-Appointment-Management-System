package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.DoctorDto;
import com.example.demo.enums.Specialization;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.PhoneNumberAlreadyExistsException;
import com.example.demo.model.Doctor;
import com.example.demo.model.User;
import com.example.demo.repository.DoctorRepository;

@Service
public class DoctorService {
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private UserService userService;
	public DoctorDto getDoctorById(String Id) {
		Optional<Doctor> doc = doctorRepository.findBydoctorId(Id);
		Optional<User> user = userService.getUserById(Id);
		DoctorDto docDetails = new DoctorDto();
		
		docDetails.setName(doc.get().getName());
		docDetails.setDoctor_id(doc.get().getDoctorId());
		docDetails.setSpecialization(doc.get().getSpecialization());
		docDetails.setEmail(user.get().getEmail());
		docDetails.setPhoneNumber(user.get().getPhoneNumber());
		docDetails.setPassword(user.get().getPassword());
		
		return docDetails;
		
	}
	
	public List<Doctor> getDoctorBySpecialization(Specialization specialization){
		List<Doctor> doctors = doctorRepository.findDoctorBySpecialization(specialization);
		return doctors;
	}
	
	public void updateDoctorDetails(DoctorDto docUpdate) {
		
		
		Doctor doctor = doctorRepository.findBydoctorId(docUpdate.getDoctor_id()).get();
		User user = userService.getUser(docUpdate.getDoctor_id());
		if(docUpdate.getName() != null) {
			doctor.setName(docUpdate.getName());
		}
		if(docUpdate.getSpecialization()!=null) {
			doctor.setSpecialization(docUpdate.getSpecialization());
		}
		if(docUpdate.getEmail()!=null) {
			if(userService.emailExist(docUpdate.getEmail())) {
				throw new EmailAlreadyExistsException("Email "+docUpdate.getEmail() +" given for Update Already Exist");
			}
			user.setEmail(docUpdate.getEmail());
		}
		if(docUpdate.getPhoneNumber()!=null) {
			if(userService.phoneNumberExist(docUpdate.getPhoneNumber())) {
				throw new PhoneNumberAlreadyExistsException("Phone Number "+ docUpdate.getPhoneNumber() +" given for Update Already Exist");
			}
			user.setPhoneNumber(docUpdate.getPhoneNumber());
		}
		if(docUpdate.getPassword()!=null) {
			user.setPassword(docUpdate.getPassword());
		}
		doctorRepository.save(doctor);
		userService.updateuser(user);
	}
}
