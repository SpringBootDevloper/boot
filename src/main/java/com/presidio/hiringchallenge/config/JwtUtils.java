package com.presidio.hiringchallenge.config;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.time.DateUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
public class JwtUtils {
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final String Developer="Madhur";
	private JwtUtils() {
		
	}
	public static String getUsernameFromToken(String jwtToken) {
		var claimsOptional= parseToken(jwtToken);
		System.out.println(claimsOptional);
		if(claimsOptional.isPresent()) {
			return(claimsOptional.get().getSubject());
		}
		return (null);
	}
     
	private static Optional<Claims> parseToken(String jwtToken) {
		var jwtParser = Jwts.parser()
                   .verifyWith(secretKey)
                   .build();
		try {
			return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
		}
		catch(JwtException e) {
			throw new RuntimeException(e);
		}
		catch(IllegalArgumentException e) {
			throw new RuntimeException(e);
		}
}

	public static boolean validateToken(String jwtToken) {
		return parseToken(jwtToken).isPresent();
	}

	public static String generateToken(String email) {
		var currentDate=new Date();
		var jwtExpirationTimeInMinutes=60;
		var expiration = DateUtils.addSeconds(currentDate,jwtExpirationTimeInMinutes*60);
		return Jwts.builder()
				.id(UUID.randomUUID().toString())
				.issuer(Developer)
				.subject(email)
				.signWith(secretKey)
				.issuedAt(currentDate)
		        .expiration(expiration).compact();
	}
}
