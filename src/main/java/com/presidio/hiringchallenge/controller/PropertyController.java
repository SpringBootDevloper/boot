package com.presidio.hiringchallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.presidio.hiringchallenge.Interface.PropertyInterface;
import com.presidio.hiringchallenge.constants.PaginationConstants;
import com.presidio.hiringchallenge.dto.PropertyDto;
import com.presidio.hiringchallenge.response.ApiResponse;
import com.presidio.hiringchallenge.response.PaginationResponse;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
@RestController
@CrossOrigin("*")
@RequestMapping("/rentify")
@Log4j2
public class PropertyController {
	@Autowired
	PropertyInterface property;
	@PostMapping("/postProperty")
	public ResponseEntity<ApiResponse> postProperty(@Valid @RequestBody PropertyDto postPropertyDto){
		try {
			log.info(postPropertyDto);
			ApiResponse propertyPosted = property.postProperty(postPropertyDto);
			return ResponseEntity.status(200).body(propertyPosted);
		}
		catch(Exception e) {
			log.error("Exception occured"+e.getMessage());
			ApiResponse error = new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			return ResponseEntity.status(500).body(error);
		}
	}
	@GetMapping("/getProperties")
	public ResponseEntity<PaginationResponse> getAllProperties(
		@RequestParam(name="pageNumber",defaultValue = PaginationConstants.PAGE_NUMBER,required = false) Integer pageNumber,
		@RequestParam(name="pageSize",defaultValue = PaginationConstants.PAGE_SIZE,required = false) Integer pageSize,
		@RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
		@RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
			) {
		PaginationResponse response = null;
		try{
			response = property.getAllProperties(pageNumber, pageSize, sortBy, sortDir);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		catch(Exception e) {
			log.error("Exception Occurred while fetching data"+e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	@PostMapping("/getSellerProperties")
	public ResponseEntity<PaginationResponse> getSellerProperies(
			@RequestBody String email,
			@RequestParam(name="pageNumber",defaultValue = PaginationConstants.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue = PaginationConstants.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = PaginationConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = PaginationConstants.SORT_DIR, required = false) String sortDir
				) {
			PaginationResponse response = null;
			try{
				response = property.getSellerProperties(pageNumber, pageSize, sortBy, sortDir,email);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			catch(Exception e) {
				log.error("Exception Occurred while fetching data"+e.getMessage());
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} 
	@PutMapping("/updateProperty")
	public ResponseEntity<ApiResponse> updateProperty(@RequestBody PropertyDto propertyDto){
		try {
			ApiResponse updatedProperty = property.updateSellerProperty(propertyDto);
			return ResponseEntity.status(200).body(updatedProperty);
		}
		catch(Exception e) {
			log.error("EXCEPTION OCCURED"+e.getMessage());
			ApiResponse error = new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			return ResponseEntity.status(500).body(error);
		}
	}
	@DeleteMapping("/deleteProperty/{id}")
	public ResponseEntity<ApiResponse> deleteProperty(@PathVariable("id") int id){
		try {
			ApiResponse updatedProperty = property.deleteProperty(id);
			return ResponseEntity.status(200).body(updatedProperty);
		}
		catch(Exception e) {
			log.error("EXCEPTION OCCURED"+e.getMessage());
			ApiResponse error = new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			return ResponseEntity.status(500).body(error);
		}
	}
	@GetMapping("/shareDetails")
	public ResponseEntity<ApiResponse> shareDetails(@RequestParam(name="email") String email,@RequestParam(name="propertyId") int propertyId){
		try {
			ApiResponse response = property.shareDetails(email,propertyId);
			return ResponseEntity.status(200).body(response);
		}
		catch(Exception e) {
			log.error("EXCEPTION OCCURED"+e.getMessage());
			ApiResponse error = new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			return ResponseEntity.status(500).body(error);		}
	}
}
