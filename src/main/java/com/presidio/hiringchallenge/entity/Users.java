package com.presidio.hiringchallenge.entity;

import java.util.List;


import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Users {
	@Id
	@Email
	String email;
	@NotNull
	@NotBlank
	@Size(min = 3, max =20 , message="Name cannot be less than 3 characters !!")
	String name;
	@NotBlank
	@Size(min=3 , message="Password cannot be empty !!")	
	String password;	
	@NotBlank
	@Size(min=10,message="Mobile number should not be less than 10")
	String phoneNumber;
	int otp;
	boolean verified;
	List<GrantedAuthority> authorities;
}
