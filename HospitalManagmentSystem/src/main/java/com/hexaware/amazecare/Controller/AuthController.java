package com.hexaware.amazecare.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.amazecare.DTO.ResponseDTO;
import com.hexaware.amazecare.DTO.UsersDTO;
import com.hexaware.amazecare.Exception.PatientNotFoundException;
import com.hexaware.amazecare.Service.AuthService;



@RestController
@RequestMapping("/api")
public class AuthController {

	
	@Autowired
	AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<ResponseDTO> login(@RequestBody UsersDTO user) {
		
		ResponseDTO login= authService.authenticateUser(user);
		return new ResponseEntity<>(login,HttpStatus.ACCEPTED);
	}
	@PutMapping("/forgotPassword/{dob}")
	public ResponseEntity<String> forgotPassword(@PathVariable LocalDate dob,@RequestBody UsersDTO user) throws PatientNotFoundException {
		
		String login= authService.forgotPassword(user,dob);
		if(login==null) {
			throw new PatientNotFoundException("No Patient Found!");
		}
		return new ResponseEntity<>(login,HttpStatus.ACCEPTED);
	}
	
}
