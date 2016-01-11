package com.appbasket.project.retailnfc;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class EncrytDecrypt {
	private static String Salt = "any data used as";
	public static String URIPrefix = "com.tagtual.beat://?d=";
	public static String APIURLPrefix = "http://tapntrack.biz/"+"newtable_offline.php";
	//String tagDataUri = ;
	  
	
	public static String DecryptData(String base64str)
	{
		byte[] encodedBytes = null;
		byte[] decodedBytes = null;
	    String FinalString = "";
        try {
        	SecretKey aesKey = new SecretKeySpec(Salt.getBytes(), "AES");
        	Cipher c = Cipher.getInstance("AES");
        	c.init(Cipher.DECRYPT_MODE, aesKey);
        	
        	base64str = URLDecoder.decode(base64str, "utf-8");
        	encodedBytes = Base64.decode(base64str,Base64.DEFAULT);
        	decodedBytes = c.doFinal(encodedBytes);
        	FinalString = new String(decodedBytes);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
		return FinalString;
	}
 
	
	public static String EncryptData(String encStr)
	{
		byte[] encodedBytes = null;
		String finalString = "";
        try {
        	SecretKey aesKey = new SecretKeySpec(Salt.getBytes(), "AES");
        	Cipher c = Cipher.getInstance("AES");
        	c.init(Cipher.ENCRYPT_MODE, aesKey);
            encodedBytes = c.doFinal(encStr.getBytes());
            finalString = Base64.encodeToString(encodedBytes,Base64.DEFAULT);
            finalString = URLEncoder.encode(finalString,"utf-8");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return finalString;
	}

}
