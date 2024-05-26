package com.presidio.hiringchallenge.Interface;

import org.springframework.http.ResponseEntity;

import com.presidio.hiringchallenge.dto.LoginDto;
import com.presidio.hiringchallenge.dto.UnverifiedUserDto;
import com.presidio.hiringchallenge.entity.Users;
import com.presidio.hiringchallenge.response.ApiResponse;

public interface UserInterface{
public String checkExistence(String email);
public int getOtp(UnverifiedUserDto unverifiedUser);
public ApiResponse registerUser(UnverifiedUserDto unverified); 
public ApiResponse sendSimpleEmail(String from, String to, int message, String subject);
public ApiResponse verifyUser(String email,int otp);
public ApiResponse loginUser(LoginDto loginDetails);
}
