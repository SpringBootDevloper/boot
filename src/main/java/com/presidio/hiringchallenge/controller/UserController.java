package com.presidio.hiringchallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.presidio.hiringchallenge.Interface.UserInterface;
import com.presidio.hiringchallenge.dto.LoginDto;
import com.presidio.hiringchallenge.dto.UnverifiedUserDto;
import com.presidio.hiringchallenge.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
@CrossOrigin("*")
@RestController
@Log4j2
@RequestMapping("/rentify")
public class UserController {
	@Autowired
	UserInterface user;

	@PostMapping("/registerUser")
	public ResponseEntity<ApiResponse> registerUnverifiedUser(@Valid @RequestBody UnverifiedUserDto unverifiedUser){
		try {
			ApiResponse signUp = user.registerUser(unverifiedUser);
			return ResponseEntity.status(200).body(signUp);
		}
		catch(Exception e) {
			ApiResponse error = new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			return ResponseEntity.status(500).body(error);
		}
	}
	@PostMapping("/verify/{email}")
	public ResponseEntity<ApiResponse> verifyUser(@PathVariable("email") String email,@RequestBody int otp){
		try {
			ApiResponse verifiedUser = user.verifyUser(email, otp);
			return ResponseEntity.status(200).body(verifiedUser);
		}
		catch(Exception e) {
			ApiResponse error = new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			return ResponseEntity.status(500).body(error);
		}
	}
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody LoginDto loginDetails){
		try {
			ApiResponse jwtToken= user.loginUser(loginDetails);
			return ResponseEntity.status(200).body(jwtToken);
		}
		catch(Exception e) {
			log.error(e.getMessage());
			return ResponseEntity.status(500).body(new ApiResponse(500,"Error while generating token",e.getMessage(),null));
		}
	}
	
}
