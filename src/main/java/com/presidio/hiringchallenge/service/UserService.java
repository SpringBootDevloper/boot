package com.presidio.hiringchallenge.service;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.presidio.hiringchallenge.producer.EmailProducer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.presidio.hiringchallenge.Interface.UserInterface;
import com.presidio.hiringchallenge.config.JwtUtils;
import com.presidio.hiringchallenge.constants.Roles;
import com.presidio.hiringchallenge.dto.LoginDto;
import com.presidio.hiringchallenge.dto.UnverifiedUserDto;
import com.presidio.hiringchallenge.entity.Users;
import com.presidio.hiringchallenge.helper.AesEncryption;
import com.presidio.hiringchallenge.repository.UserRepo;
import com.presidio.hiringchallenge.response.ApiResponse;

import lombok.extern.log4j.Log4j2;


@Service
@Log4j2
public class UserService implements UserInterface {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	UserRepo userRepo;
	@Autowired
	ModelMapper modelmapper;
	@Autowired
	AesEncryption encrytion;
    @Autowired
    AuthenticationManager authenticationManager;
	@Autowired
	EmailProducer emailProducer;
	@Value("${from}")
	String from;

	@Override
	public String checkExistence(String email) {
		try {
			Optional<Users> user = userRepo.findById(email);
			if (user.isPresent()) {
				Users isPresent = user.get();
				if (isPresent.isVerified())
					return "User already registered";
			}
			return "New User OTP will be sent";
		} catch (Exception e) {
			log.error(e.getMessage());
			return "Exception Occured";
		}
	}

	@Override
	public int getOtp(UnverifiedUserDto unverifiedUser) {
		try {
			int otpLength = 6;
			String otp = "";
			int generatedOTP;
			for (int i = 0; i < otpLength; i++) {
				int digit = 0;
				digit = (int) (Math.random() * 10);
				otp += (digit);
			}
			generatedOTP = Integer.parseInt(otp);
			Users unverified = UnverifiedUserDtoToUser(unverifiedUser);
			unverified.setEmail(unverifiedUser.getEmail());
			unverified.setPhoneNumber(unverifiedUser.getPhoneNumber());
			unverified.setName(unverifiedUser.getName());
			Key key = AesEncryption.generateKey();
			String encryptValue = AesEncryption.encrypt(unverifiedUser.getPassword(), key);
			unverified.setPassword(encryptValue);
			unverified.setOtp(generatedOTP);
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(Roles.ROLE_BOTH.name()));
			unverified.setAuthorities(authorities);
			unverified.setVerified(false);
			log.info(unverified.toString());
			log.info("fatega");
			userRepo.save(unverified);
			return generatedOTP;
		} catch (Exception e) {
			log.error("EXCEPTION OCCURED" + e.getMessage());
			e.printStackTrace();
			return -999999;
		}
	}

	public boolean verifyOtp(int generatedOTP, int userOtp) {
		String first = String.valueOf(generatedOTP);
		if (first.length() != 6)
			return false;
		String second = String.valueOf(userOtp);
		for (int i = 0; i < first.length(); i++) {
			if (first.charAt(i) != second.charAt(i))
				return false;
		}
		return true;
	}

	public Users UnverifiedUserDtoToUser(UnverifiedUserDto unverifiedUser) {
		Users user = this.modelmapper.map(unverifiedUser, Users.class);
		return user;
	}

	@Override
	public ApiResponse registerUser(UnverifiedUserDto unverified) {
		try {
			Users user = UnverifiedUserDtoToUser(unverified);
			String status = checkExistence(user.getEmail());
			if (status.equals("User already registered")) {
				log.info("User already registered");
				return new ApiResponse(201, "User already registered", "null", null);
			} else if (status.equals("New User OTP will be sent")) {
				log.info("New User OTP will be sent");
				int generatedOtp = getOtp(unverified);
				log.info("OTP Generated and sent");
				ApiResponse mailResponse = sendSimpleEmail(from,user.getEmail(),generatedOtp,"Otp for Email Verification");
				return new ApiResponse(200, "Otp sent to your email " + unverified.getEmail(), "null", null);
			} else {
				return new ApiResponse(500, "INTERNAL SERVER ERROR", "INTERNAL SERVER ERROR", null);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ApiResponse(500, "INTERNAL SERVER ERROR", e.getMessage(), null);
		}
	}

	@Override
	public ApiResponse sendSimpleEmail(String from, String to, int message, String subject) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			String otp = String.valueOf(message);
			String content = "Welcome to Rentify, your go-to destination for buying and selling properties with ease!\n\n"
					+ "To ensure the security of your account and start exploring properties with us, please verify your email address by entering the OTP (One-Time Password) provided below:\n\n"
					+ "OTP: " + otp + "\n\n"
					+ "Please enter the above OTP in the verification field on our website to complete the registration process. This OTP will be valid for the next 10 minutes.\n\n"
					+ "Thank you for choosing Rentify. We're excited to have you on board!\n\n" + "Best regards,\n"
					+ "The Rentify Team";
			simpleMailMessage.setTo(to);
			simpleMailMessage.setText(content);
			simpleMailMessage.setFrom(from);
			simpleMailMessage.setSubject(subject);
			mailSender.send(simpleMailMessage);
			log.info("Mail Object created");
			return new ApiResponse(200, "Otp sent to registered mail", "", null);
		} catch (Exception e) {
			log.error("Error while sending email", e);
			return new ApiResponse(500, "INTERNAL SERVER ERROR", e.getMessage(), null);
		}

	}

	@Override
	public ApiResponse verifyUser(String email, int otp) {
		try {
			Optional<Users> unverifiedUser = userRepo.findById(email);
			if (unverifiedUser.isPresent()) {
				Users verifyUser = unverifiedUser.get();
				if (verifyOtp(verifyUser.getOtp(), otp)) {
					log.info("OTP VERIFIED");
					verifyUser.setEmail(email);
					verifyUser.setPassword(verifyUser.getPassword());
					verifyUser.setName(verifyUser.getName());
					verifyUser.setOtp(otp);
					verifyUser.setPhoneNumber(verifyUser.getPhoneNumber());
					List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					authorities.add(new SimpleGrantedAuthority(Roles.ROLE_BOTH.name()));
					verifyUser.setAuthorities(authorities);
					userRepo.save(verifyUser);
					emailProducer.sendEmailMessageToExchange("User Details Saved "+email);
					return new ApiResponse(200, "USER VERIFIED", "null", null);
				}
				else 
					return new ApiResponse(500, "WRONG OTP", "null", null);
			}
			else
			return new ApiResponse(404, "User with this email id not found", "User not found", null);
		}
		catch (Exception e) {
			return new ApiResponse(500, "INTERNAL SERVER ERROR", e.getMessage(), null);
		}
	}
	@Override
	public ApiResponse loginUser(LoginDto login) {
		try {
			var authToken= new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword());
			var authenticate = authenticationManager.authenticate(authToken);
			String token = JwtUtils.generateToken(((UserDetails)(authenticate.getPrincipal())).getUsername());
			return new ApiResponse(200,"Login Successfull / Token Generated","null",token);
		}
		catch(Exception e) {
			log.error("Error while generating Token "+e.getMessage());
			return new ApiResponse(500,"INTERNAL SERVER ERROR",e.getMessage(),null);
		}
    }
}
