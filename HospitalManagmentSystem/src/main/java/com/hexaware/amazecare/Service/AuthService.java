package com.hexaware.amazecare.Service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaware.amazecare.DTO.ResponseDTO;
import com.hexaware.amazecare.DTO.UsersDTO;
import com.hexaware.amazecare.Model.Doctor;
import com.hexaware.amazecare.Model.Patient;
import com.hexaware.amazecare.Model.Users;
import com.hexaware.amazecare.Repository.DoctorRepo;
import com.hexaware.amazecare.Repository.PatientRepo;
import com.hexaware.amazecare.Repository.UserRepo;
import com.hexaware.amazecare.Security.JWTService;

@Service
public class AuthService {


	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JWTService jwtService;
	
	@Autowired
	PatientRepo patientRepo;
	
	@Autowired
	DoctorRepo doctorRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserInfoUserDetailsService userService;
	
	Logger logger= LoggerFactory.getLogger(AuthService.class);
	
	public ResponseDTO authenticateUser(UsersDTO user) {
		
		logger.info("Request initiated to Login by User: "+user.getUsername());
		
		Users u=userRepo.findByUsername(user.getUsername()).orElse(null);
		Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword()));
		if(authentication.isAuthenticated() && u!=null) {
			logger.info("Login Successfull!");
			ResponseDTO response=new ResponseDTO();
			String jwt=jwtService.generateToken(userService.loadUserByUsername(user.getUsername()));
			response.setJwt(jwt);
			if(u.getRole().name().equals("PATIENT")) {
				response.setUserid(patientRepo.findByEmail(u.getUsername()).get().getPatientId());
			}else if(u.getRole().name().equals("DOCTOR")) {
				response.setUserid(doctorRepo.findByEmail(u.getUsername()).get().getDoctorId());
			}else {
				response.setUserid(u.getUserId());
			} 
			response.setUsername(u.getUsername());
			response.setRole(u.getRole().name());
			return response;
		}else {
			logger.warn("Login Failed: Invalid Credentials!");
			throw new UsernameNotFoundException("Invalid Username and Password");
		}
	
	}

	public String forgotPassword(UsersDTO user, LocalDate dob) {
		Users u=userRepo.findByUsername(user.getUsername()).orElse(null);
		if(u==null) {
			return null;	
		}else {
			if(u.getRole().name().equals("PATIENT")) {
				Patient patient=patientRepo.findByEmail(u.getUsername()).get();
				if(user.getPassword()!=null && patient.getDateOfBirth().equals(dob)) {
			    	u.setPassword(passwordEncoder.encode(user.getPassword()));
			    	patient.setUser(u);
			        userRepo.save(u); 
			        patientRepo.save(patient);
			        return "Password reset Successfull!";
				}else {
					return null;
				}
			}else if(u.getRole().name().equals("DOCTOR")) {
				return "DOCTOR";
			}else {
				return "ADMIN";
			} 
		}
	}

}
