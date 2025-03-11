package com.example.demo.service;

//import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.UserDto;
import com.example.demo.enums.Role;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.PasswordMismatchException;
import com.example.demo.exceptions.PhoneNumberAlreadyExistsException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

//import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
		@Autowired
		private UserRepository userrepo;
		 
		 @Transactional
		    public void signUp(User u) {
		       // user.setPassword(passwordEncoder.encode(user.getPassword()));
			     
		        if(userrepo.findByEmail(u.getEmail()).isPresent()) {
		        	throw new EmailAlreadyExistsException("Email "+u.getEmail()+" Already Exist");
		        }
		        if(userrepo.findByPhoneNumber(u.getPhoneNumber()).isPresent()) {
		        	throw new PhoneNumberAlreadyExistsException("Phone Number "+u.getPhoneNumber()+" Already Exist");
		        }
		        if (u.getRole().equals(Role.DOCTOR)) {
		            Doctor doctor = u.getDoctor();
		            doctor.setUserDoctor(u);
		            u.setDoctor(doctor);
		            // Save User entity, Doctor will be saved because of CascadeType.ALL
		            userrepo.save(u);
		        } else if (u.getRole().equals(Role.PATIENT)) {
		            Patient patient = u.getPatient();
		            patient.setUserPatient(u);
		            u.setPatient(patient);
		            // Save User entity, Patient will be saved because of CascadeType.ALL
		            userrepo.save(u);
		        }
		 }
		 public boolean emailExist(String email) {
			 if(userrepo.findByEmail(email).isPresent()) {
				 return true;
			 }
			 return false;
		 }
		 public boolean phoneNumberExist(String phoneNumber) {
			 if(userrepo.findByPhoneNumber(phoneNumber).isPresent()) {
				 return true;
			 }
			 return false;
		 }
		 public Optional<User> signIn(String identifier, String password) {
		        Optional<User> user = userrepo.findByEmail(identifier);
		        
		        if (!user.isPresent()) {
		        	 user = userrepo.findByPhoneNumber(identifier);
		        }
		        if(user.isEmpty()) {
		        	throw new UserNotFoundException("No user Found with "+identifier);
		        }
		        if (!user.get().getPassword().equals(password)) {
		            throw new PasswordMismatchException("Incorrect password");
		        }
		        return user;
		    }
		 
		 
		public Optional<User> getUserById(String Id) {
			
			Optional<User> user = userrepo.findById(Id);
			if(!user.isPresent())
				throw new UserNotFoundException("User with Id: "+Id+" Not Found");
			
			return user;
			
		}
		public User getUser(String Id) {
			
			User user = userrepo.findById(Id).get();
			return user;
		}
		public void updateuser(User user) {
			userrepo.save(user);
		}
		
		public void deleteUser(String id) {
			Optional<User> user = userrepo.findById(id);
			if(!user.isPresent())
				throw new UserNotFoundException("User with Id: "+id+" Not Found");
			
			userrepo.deleteById(id);
		}
}
