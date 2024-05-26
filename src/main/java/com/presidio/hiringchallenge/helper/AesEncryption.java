package com.presidio.hiringchallenge.helper;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
@Component
public class AesEncryption {		
	    
		private static final String ALGORITHM = "AES";
	    private static final byte[] keyValue = "1234567891234567".getBytes();
	        
	 
	    public static Key generateKey()  {
	        Key key = new SecretKeySpec(keyValue, ALGORITHM);
	        return key;
	    }
	    
	    public static String encrypt(String valueToEnc, Key key) {
	    	 
	    	System.out.println("valuetoEnc----"+valueToEnc);
	        Cipher cipher = null;
			try {
				cipher = Cipher.getInstance(ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				cipher.init(Cipher.ENCRYPT_MODE, key);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  
	        byte[] encValue = null;
			try {
				encValue = cipher.doFinal(valueToEnc.getBytes());
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        byte[] encryptedByteValue = new Base64().encode(encValue);
	        System.out.println("Encrypted Value :: " + new String(encryptedByteValue));
	        System.out.println("Encrypted Value :: " + decrypt(new String(encryptedByteValue), key));
	        return new String(encryptedByteValue);
	    }
	    
	    public static String decrypt(String encryptedValue, Key key){
	        // Key key = generateKey();
	    	System.out.println(encryptedValue);
	         Cipher cipher = null;
			try {
				cipher = Cipher.getInstance(ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         try {
				cipher.init(Cipher.DECRYPT_MODE, key);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          
	         byte[] decodedBytes = new Base64().decode(encryptedValue.getBytes());
	  
	         byte[] enctVal = null;
			try {
				enctVal = cipher.doFinal(decodedBytes);
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
	         System.out.println("Decrypted Value :: " + new String(enctVal));
	         return new String(enctVal);
	     }
	    
	    }