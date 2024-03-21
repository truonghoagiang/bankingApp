package com.banking.bankingapp.utils;

import java.util.Random;

public class OtpGenerate {

    public static String generateOtp(){
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        int count = 0;
        while(count < 4){
            otp.append(random.nextInt(10));
            ++count;
        }
        return otp.toString();
    }
}
