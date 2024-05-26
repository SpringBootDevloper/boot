package com.presidio.hiringchallenge.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.presidio.hiringchallenge.Interface.PropertyInterface;
import com.presidio.hiringchallenge.dto.PropertyDto;
import com.presidio.hiringchallenge.entity.Property;
import com.presidio.hiringchallenge.entity.Users;
import com.presidio.hiringchallenge.exceptions.ResourceNotFoundException;
import com.presidio.hiringchallenge.repository.PropertyRepo;
import com.presidio.hiringchallenge.repository.UserRepo;
import com.presidio.hiringchallenge.response.ApiResponse;
import com.presidio.hiringchallenge.response.PaginationResponse;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PropertyService implements PropertyInterface{
	@Autowired
	ModelMapper modelmapper;
	@Autowired
	PropertyRepo propertyRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	private JavaMailSender mailSender;
	@Value("${from}")
	String from;
	@Override
	public ApiResponse postProperty(PropertyDto PropertyDto) {
		try {
		   Property property = PropertyDtoToProperty(PropertyDto);
		   log.info(property);
		   property.setListedOn(new Timestamp(System.currentTimeMillis()));
		   Property propertySaved = propertyRepo.save(property);
		   return new ApiResponse(200,"Property Saved","null",propertySaved);
		}
		catch(Exception e) {
			log.error("Exception occured while saving property data"+e.getMessage());
			return new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
		}
	}
	public Property PropertyDtoToProperty(PropertyDto PropertyDto) {
		Property property = this.modelmapper.map(PropertyDto, Property.class);
		return property;
	}
	@Override
	public PaginationResponse getAllProperties(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		try
		{
			Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

	        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

	        Page<Property> pagePost = propertyRepo.getPageableData(p);

	        List<Property> allPosts = pagePost.getContent();

	        List<PropertyDto> postDtos = allPosts.stream().map((property) -> modelmapper.map(property, PropertyDto.class))
	                .collect(Collectors.toList());

	        PaginationResponse paginationResponse = new PaginationResponse();
            log.info(postDtos);
	        paginationResponse.setContent(postDtos);
	        paginationResponse.setPageNumber(pagePost.getNumber());
	        paginationResponse.setPageSize(pagePost.getSize());
	        paginationResponse.setTotalElements(pagePost.getTotalElements());

	        paginationResponse.setTotalPages(pagePost.getTotalPages());
	        paginationResponse.setLastPage(pagePost.isLast());
	        return paginationResponse;
	}
		catch(Exception e) {
			log.error("Exception occured while getting data from DB "+e.getMessage());
			PaginationResponse paginationResponse = new PaginationResponse();
			return paginationResponse;
		}
	}
	@Override
	public PaginationResponse getSellerProperties(Integer pageNumber, Integer pageSize, String sortBy, String sortDir,String email) {
		try
		{
			Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

	        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

	        Page<Property> pagePost = propertyRepo.getSellerProperties(p,email);

	        List<Property> allPosts = pagePost.getContent();

	        List<PropertyDto> postDtos = allPosts.stream().map((property) -> modelmapper.map(property, PropertyDto.class))
	                .collect(Collectors.toList());

	        PaginationResponse paginationResponse = new PaginationResponse();
            log.info(postDtos);
	        paginationResponse.setContent(postDtos);
	        paginationResponse.setPageNumber(pagePost.getNumber());
	        paginationResponse.setPageSize(pagePost.getSize());
	        paginationResponse.setTotalElements(pagePost.getTotalElements());

	        paginationResponse.setTotalPages(pagePost.getTotalPages());
	        paginationResponse.setLastPage(pagePost.isLast());
	        return paginationResponse;
	}
		catch(Exception e) {
			log.error("Exception occured while getting data from DB "+e.getMessage());
			PaginationResponse paginationResponse = new PaginationResponse();
			return paginationResponse;
		}
	}
	@Override
	public ApiResponse updateSellerProperty(PropertyDto propertyDto) {
		try {
			   Property property = PropertyDtoToProperty(propertyDto);
			   log.info(property);
			   property.setListedOn(new Timestamp(System.currentTimeMillis()));
			   propertyRepo.save(property);
			   return new ApiResponse(200,"Property Saved","null",property);
			}
			catch(Exception e) {
				log.error("Exception occured while saving property data"+e.getMessage());
				return new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
			}
	}
	@Override
	public ApiResponse deleteProperty(int id) {
		Property property = propertyRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "Id", id));
		propertyRepo.delete(property);
		return new ApiResponse(200, "Details Deleted", "null", property);
	}
	@Override
	public ApiResponse shareDetails(String email, int id) {
		try {
			Property property = propertyRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "Id", id));
			Users seller = userRepo.findByEmail(property.getPostedBy());
			Users buyer = userRepo.findByEmail(email);
			log.info("Details of buyer "+buyer);
			log.info("Details of seller "+seller);
			ApiResponse sendMailToBuyer = sendSimpleEmail(from,property.getPostedBy(),buyer,"Property Viewed");
				return sendSimpleEmail(from,email,seller,"Property viewed");
		}
		catch(Exception e) {
			log.error("EXCPTION OCCURED WHILE SENDING MAIL"+e.getMessage());
			return new ApiResponse(500, "INTERNAL SERVER ERROR", e.getMessage(), null);
		}
	}
	
	public ApiResponse sendSimpleEmail(String from, String to, Users details, String subject) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			String name = String.valueOf(details.getName());
			String email = String.valueOf(details.getEmail());
			String number = String.valueOf(details.getPhoneNumber());
			 String content = "Welcome to Rentify, your go-to destination for buying and selling properties with ease!\n\n"
			            + "For your reference, here are details:\n"
			            + "Email: " + email + "\n"
			            + "Phone Number: " + number + "\n\n"
			            + "Name: "+ name +"\n\n"
			            + "Thank you for choosing Rentify.\n\n"
			            + "Best regards,\n"
			            + "The Rentify Team";
			simpleMailMessage.setTo(to);
			simpleMailMessage.setText(content);
			simpleMailMessage.setFrom(from);
			simpleMailMessage.setSubject(subject);
			mailSender.send(simpleMailMessage);
			log.info("Mail Object created");
			return new ApiResponse(200, "Mail Sent to "+name, "", null);
		} catch (Exception e) {
			log.error("Error while sending email", e);
			return new ApiResponse(500, "INTERNAL SERVER ERROR", e.getMessage(), null);
		}

	}
}
