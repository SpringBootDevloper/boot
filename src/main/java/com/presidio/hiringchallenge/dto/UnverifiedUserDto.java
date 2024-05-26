package com.presidio.hiringchallenge.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnverifiedUserDto {
	@Id
	@Email
	String email;
	@NotNull
	@NotBlank
	@Size(min = 3, max =20 , message="First name cannot be less than 3 characters !!")
	String name;
	@Size(min=10,message="Mobile number should not be less than 10")
	String phoneNumber;
	@Size(min=3 , message="Password cannot be empty !!")	
	String password;
//	int otp;
//	boolean verified;
//	List<GrantedAuthority> authorities;
}
