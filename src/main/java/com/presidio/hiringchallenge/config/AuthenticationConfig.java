package com.presidio.hiringchallenge.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.presidio.hiringchallenge.helper.AesPasswordEncoder;

@Configuration
public class AuthenticationConfig {
	@Autowired
	AesPasswordEncoder passwordEnocder;
	 @Bean
	 public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		 return config.getAuthenticationManager();
	 }
	 @Bean
	 public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,AesPasswordEncoder passwordEncoder) {
		 var AuthenticationProvider = new DaoAuthenticationProvider();
		 AuthenticationProvider.setUserDetailsService(userDetailsService);
		 AuthenticationProvider.setPasswordEncoder(passwordEncoder);
		 return AuthenticationProvider;
	 }
}
