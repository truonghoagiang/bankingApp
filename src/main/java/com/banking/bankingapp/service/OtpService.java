package com.banking.bankingapp.service;

import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.entity.OtpEntity;
import com.banking.bankingapp.payload.request.OtpRequest;
import com.banking.bankingapp.payload.response.LoginResponse;
import com.banking.bankingapp.payload.response.OtpResponse;
import com.banking.bankingapp.repositoty.OtpRepository;
import com.banking.bankingapp.service.Imp.EmailServiceImp;
import com.banking.bankingapp.utils.OtpGenerate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private EmailServiceImp emailServiceImp;

    public LoginResponse sendOtp(OtpRequest otpRequest){
        //generate the otp
        //send it via email
        //save it to db
        //check if OTP exists in DB
        OtpEntity otpcheck = otpRepository.findByEmail(otpRequest.getEmail());
        if(otpcheck != null){
            otpRepository.delete(otpcheck);
        }
        String otp = OtpGenerate.generateOtp();
        log.info("Check OTP:", otp);
        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setOtp(otp);
        otpEntity.setExpireAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntity);
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("DO NOT DISCLOSE!");
        emailDetails.setRecipient(otpRequest.getEmail());
        emailDetails.setMessageBody("Your OTP to login into the system. This OTP expires in 5 minutes:\n" + otp);
        emailServiceImp.sendEmailAlert(emailDetails);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatusCode(200);
        loginResponse.setResponseMessage("SUCCESS");
        return loginResponse;
    }

    public LoginResponse validateOtp(OtpValidationRequest otpValidationRequest){
        OtpEntity otpEntity = otpRepository.findByEmail(otpValidationRequest.getEmail());
        log.info("Email check: ", otpValidationRequest.getEmail());
        LoginResponse loginResponse = new LoginResponse();
        if(otpEntity == null){
            loginResponse.setStatusCode(400);
            loginResponse.setResponseMessage("You have not sent the OTP");
        }
        if(otpEntity.getExpireAt().isBefore(LocalDateTime.now())){
            loginResponse.setStatusCode(400);
            loginResponse.setResponseMessage("OTP expired");
        }
        if(!otpEntity.getOtp().equals(otpValidationRequest.getOtp())){
            loginResponse.setStatusCode(400);
            loginResponse.setResponseMessage("Invalid OTP");
        }
        else{
            loginResponse.setStatusCode(200);
            loginResponse.setResponseMessage("SUCCESS");
            loginResponse.setOtpResponse(OtpResponse.builder()
                    .isOtpValid(true)
                    .build());
        }
        return loginResponse;
    }
}
