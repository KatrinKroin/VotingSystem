package com.project.katri.votingsce;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Random;


public class BallotKeyBuilder {
    Context context;

    public BallotKeyBuilder(Context context){
        this.context = context;
    }


    public String BuildKey(){
        String operator = get_operator();
        String time =  get_timestamp();
        String prime =  get_prime();
        String msg = AES.keygen(string_xor(operator,time),prime);

        return msg;
    }

    private String get_operator(){
        String operator;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return  "error";
        }
        operator = tm.getSimOperator();
        if (TextUtils.isEmpty(operator)) {
            operator = "-1";
        }
        return fill_128(operator);
    }

    private String get_timestamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return fill_128(String.valueOf(timestamp.getTime()));
    }


    private String get_prime() {
        Random ran = new SecureRandom();
        String prime = String.valueOf(BigInteger.probablePrime(128, ran));  //
        byte[] b = prime.getBytes();
        String s = "";
        for(int i = 0;i<b.length;i++)
            s += String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
        return s.substring(0,128);
    }

    private String fill_128(String text){
        Random rand = new Random();
        for(int i = 0;i<16;i++) {
            int n = rand.nextInt(256);
            int m = rand.nextInt(text.length());
            text = text.substring(0,m) + (char)n + text.substring(m);
            if(text.length() >= 16)
                break;
        }
        byte[] b = text.getBytes();
        String s = "";
        for(int i = 0;i<b.length;i++)
            s += String.format("%8s", Integer.toBinaryString(b[i] & 0xFF)).replace(' ', '0');
        return s.substring(0,128);
    }

    private String string_xor(String str1, String str2){
        byte[] b1 = new BigInteger(str1, 2).toByteArray();
        byte[] b2 = new BigInteger(str2, 2).toByteArray();
        String s = "";
        for(int i=0;i<b1.length & i<b2.length; i++)
            s += String.format("%8s", Integer.toBinaryString(b1[i]^b2[i] & 0xFF)).replace(' ', '0');
        return s;
    }
}
