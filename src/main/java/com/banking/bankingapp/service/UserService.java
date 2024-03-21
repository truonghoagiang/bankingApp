package com.banking.bankingapp.service;

import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.payload.response.BankResponse;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.EmailServiceImp;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import com.banking.bankingapp.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService implements UserServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailServiceImp emailServiceImp;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> createAccount(UserDTO userDTO) {
        /**
         * creating an account - save it to db
         * check if user already had an account
         */
        if(userRepository.existsByEmail(userDTO.getEmail())){
            BankResponse bankResponse = new BankResponse();
            bankResponse.setResponseCode(HttpStatus.BAD_REQUEST.toString());
            bankResponse.setResponseMessage("Email has already taken!");
            bankResponse.setAccountInfo(null);
            return new ResponseEntity(bankResponse, HttpStatus.BAD_REQUEST);
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
//                    .accountInfo(null)
//                    .build();
        }

        UserEntity user = new UserEntity();
        user.setIdnumber(userDTO.getIdnumber());
        user.setFullname(userDTO.getFullname());
        user.setOthername(userDTO.getOthername());
        user.setGender(userDTO.getGender());
        user.setAddress(userDTO.getAddress());
        user.setStateOfOrigin(userDTO.getStateOfOrigin());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPhonenumber(userDTO.getPhonenumber());
        user.setStatus("ACTIVE");
        user.setAccountNumber(AccountUtils.generateAccountNumber());
        user.setAccountBalance(BigDecimal.ZERO);
        UserEntity saveUser = userRepository.save(user);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation! Your account has been successfully created. \nYour Account Details: \n" +
                        "Account Name: " + saveUser.getFullname() + "\nAccount Number: " + saveUser.getAccountNumber())
                .build();
        emailServiceImp.sendEmailAlert(emailDetails);
        BankResponse bankResponse = new BankResponse();
        bankResponse.setResponseCode(HttpStatus.CREATED.toString());
        bankResponse.setResponseMessage("Account registered suscessfully");

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountName(saveUser.getFullname());
        accountInfo.setAccountNumber(saveUser.getAccountNumber());
        accountInfo.setAccountBalance(saveUser.getAccountBalance());

        bankResponse.setAccountInfo(accountInfo);

        return new ResponseEntity(bankResponse, HttpStatus.OK);
    }

}
