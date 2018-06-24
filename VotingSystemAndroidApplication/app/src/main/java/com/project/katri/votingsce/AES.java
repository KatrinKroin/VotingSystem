package com.project.katri.votingsce;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    //private static final String secret = "_-66$pP6&ZP<mM_jp?";
    private static final String secret = "rc*Ku2adLhFDS#K@ZV=pT2TUqkz&3V]}";
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        try{
            key = myKey.getBytes("UTF-8");
            secretKey = new SecretKeySpec(key,"AES");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static void setBallotKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
            byte[] a= android.util.Base64.encode(cipher.doFinal(b), android.util.Base64.DEFAULT);
            return new String(a);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
            return e.toString();
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
            byte[] b = android.util.Base64.decode(strToDecrypt.getBytes("UTF-8"),android.util.Base64.DEFAULT);
            byte[] a = cipher.doFinal(b);
            return new String(a);
        }
        catch (Exception e)
        {
            return "Wrong Key!!";
        }
    }

    public static String keygen(String strToEncrypt, String key )
    {
        try
        {
            setBallotKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return android.util.Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes()), 0).substring(0,128);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
            return e.toString();
        }
    }
}