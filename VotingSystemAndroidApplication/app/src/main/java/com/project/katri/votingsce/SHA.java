package com.project.katri.votingsce;

/**
 * Created by Katri on 09/03/2018.
 */
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
    public String sha(String str) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i<hash.length;i++){
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) buffer.append('0');
            buffer.append(hex);
        }
        return buffer.toString();
       // str = new String(hash, StandardCharsets.UTF_8);
       // return str;
    }
}
