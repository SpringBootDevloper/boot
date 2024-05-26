package com.presidio.hiringchallenge.dto;

import java.util.List;


import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {
	@Id
	@Email
	String email;
	@NotNull
	@NotBlank
	@Size(min = 3, max =20 , message="First name cannot be less than 3 characters !!")
	@NotNull
	@NotBlank
	@Size(min=3 ,max=20, message="Last name cannot be less than 3 characters !!")	
	String name;
	@Size(min=10,message="Mobile number should not be less than 10")
	String phoneNumber;
	int otp;
	boolean verified;
	List<GrantedAuthority> authorities;
}
