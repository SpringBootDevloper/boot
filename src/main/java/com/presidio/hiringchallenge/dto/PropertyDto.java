package com.presidio.hiringchallenge.dto;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int propertyId;

	@Email
	String postedBy;

	@NotNull
	@NotBlank
	@Size(min = 3, max = 20, message = "Property Name cannot be less than 3 characters !!")
	String propertyName;

	@NotNull
	@NotBlank
	@Size(min = 3, message = "Address cannot be empty !!")
	String address;

	@NotNull
	@NotBlank
	@Size(min = 3, message = "City cannot be empty !!")
	String city;
	
	
	@Lob
	byte[] propertyImage;	
	
	@NotNull
    @Min(value = 1, message = "Price must be at least 1")
    int price;

	@NotNull
	@Size(min = 3, message = "City cannot be empty !!")
	String description;
	
	boolean isActive = true;
	String type;
	int bathroom;
	int bedroom;
	String landmark;
}
