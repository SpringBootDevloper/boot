package com.presidio.hiringchallenge.entity;

import java.sql.Timestamp;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Property {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int propertyId;

	@Email
	String postedBy;

	@NotNull
	@Size(min = 3, max = 20, message = "Property Name cannot be less than 3 characters !!")
	String propertyName;

	@NotNull
	@Size(min = 3, message = "Address cannot be empty !!")
	String address;

	@NotNull
	@Size(min = 3, message = "City cannot be empty !!")
	String city;
	
	@Basic
	@Column(length = 1048576)
	@Lob
	byte[] propertyImage;
	
	@NotNull
    @Min(value = 1, message = "Price must be at least 1")
    int price;
	
	@NotNull
	@Size(min = 3, message = "Description cannot be empty !!")
	String description;

	boolean isActive;
	String type;
	Timestamp listedOn;
	int bathroom;
	int bedroom;
	String landmark;
	Long views;
	Long likes;
}
