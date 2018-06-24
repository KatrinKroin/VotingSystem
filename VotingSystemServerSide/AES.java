package com.example;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

 
public class AES {
 
	private static final String secret = "rc*Ku2adLhFDS#K@ZV=pT2TUqkz&3V]}";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        try {       	
            key = myKey.getBytes("UTF-8");	            
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String key )
    {
        try
        {
        	if(key == null || key == "")
        		setKey(secret);
        	else 
        		setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] b = strToEncrypt.getBytes("UTF-8");   
            byte[] a = Base64.getMimeEncoder().encode(cipher.doFinal(b));
            return new String(a);
        }
        catch (Exception e)
        {
        	return e.getMessage();
        }
    }
    

 
    public static String decrypt(String strToDecrypt, String key)
    {

        try
        {
        	if(key == null || key == "")
        		setKey(secret);
        	else 
        		setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] b = Base64.getMimeDecoder().decode(strToDecrypt.getBytes("UTF-8"));
            byte[] a = cipher.doFinal(b);
        	return new String(a);

        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
    
    
}