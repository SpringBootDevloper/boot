package com.presidio.hiringchallenge.config;


import java.io.IOException;
import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.presidio.hiringchallenge.helper.AesEncryption;
import com.presidio.hiringchallenge.helper.AesPasswordEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {	
	private static final long serialVersionUID = -7858869558953243875L;
	@Override
	public void commence(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
	}
	@Bean
    public AesEncryption aesEncryption() {
        return new AesEncryption();
    }

    @Bean
    public PasswordEncoder passwordEncoder(AesEncryption aesEncryption) {
    	System.out.println("AES Encrytion object received --->"+aesEncryption);
        return new AesPasswordEncoder(aesEncryption);
    }
}
