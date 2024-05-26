package com.presidio.hiringchallenge.helper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

import com.presidio.hiringchallenge.repository.UserRepo;

import java.security.Key;
import java.util.Arrays;

@Component
public class AesPasswordEncoder implements PasswordEncoder {

	@Autowired
	UserRepo register;
	private final AesEncryption aesEncryption;

    public AesPasswordEncoder(AesEncryption aesEncryption) {
        this.aesEncryption = aesEncryption;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // Generate AES key
        Key key = AesEncryption.generateKey();
        // Encrypt the raw password
        return AesEncryption.encrypt(rawPassword.toString(), key);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Generate AES key
        Key key = aesEncryption.generateKey();
//    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = ((UserDetails)principal).getUsername();

//if (principal instanceof UserDetails) {
//} else {
//  String username = principal.toString();
//}
        
//        Register user = register.findByEmail(username);	
        // Decrypt the encoded password
        System.out.println("Encodec 00909090090--------?>       "+encodedPassword);
        System.out.println("rawpwd 00909090090--------?>       "+rawPassword);

        String decryptedPassword = aesEncryption.decrypt(encodedPassword, key);
        System.out.println("decpwd 00909090090--------?>       "+decryptedPassword);

        // Compare the decrypted password with the raw password
        boolean matched = Arrays.equals(decryptedPassword.getBytes(), rawPassword.toString().getBytes());

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Decrypted Password: " + decryptedPassword);
        System.out.println("Matched or not: " + matched);

        return matched;
    }}



