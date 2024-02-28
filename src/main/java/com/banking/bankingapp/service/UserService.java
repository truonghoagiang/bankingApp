package com.banking.bankingapp.service;

import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.EmailServiceImp;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import com.banking.bankingapp.utils.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailServiceImp emailServiceImp;

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Override
    public BankResponse createAccount(UserDTO userDTO) {
        /**
         * creating an account - save it to db
         * check if user already had an account
         */

        if(userRepository.existsByEmail(userDTO.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
//        UserEntity user = UserEntity.builder()
//                .firstname(userDTO.getFirstname())
//                .lastname(userDTO.getLastname())
//                .othername(userDTO.getOthername())
//                .gender(userDTO.getGender())
//                .address(userDTO.getAddress())
//                .stateOfOrigin(userDTO.getStateOfOrigin())
//                .accountnumber(AccountUtils.generateAccountNumber())
//                .accountBalance(BigDecimal.ZERO)
//                .email(userDTO.getEmail())
//                .phonenumber(userDTO.getPhonenumber())
//                .alternativePhoneNumber(userDTO.getAlternativePhoneNumber())
//                .status("ACTIVE")
//                .build();
        UserEntity user = new UserEntity();
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setOthername(userDTO.getOthername());
        user.setGender(userDTO.getGender());
        user.setAddress(userDTO.getAddress());
        user.setStateOfOrigin(userDTO.getStateOfOrigin());
        user.setEmail(userDTO.getEmail());
        user.setPhonenumber(userDTO.getPhonenumber());
        user.setAlternativePhoneNumber(userDTO.getAlternativePhoneNumber());
        user.setStatus("ACTIVE");
        user.setAccountNumber(AccountUtils.generateAccountNumber());
        user.setAccountBalance(BigDecimal.ZERO);

        UserEntity saveUser = userRepository.save(user);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation! Your account has been successfully created. \nYour Account Details: \n" +
                        "Account Name: " + saveUser.getFirstname() + " " + saveUser.getLastname() + " " + saveUser.getOthername() + "\nAccount Number: " + saveUser.getAccountNumber())
                .build();
        emailServiceImp.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstname() + " " + saveUser.getLastname() + " " + saveUser.getOthername())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry) {
        logger.info("Check account number:" + balanceEnquiry.getAccountNumber());
        //check if the provided number exists in the db
        //boolean isAccountExists = userRepository.existsByAccountNumber(enquiry.getAccountNumber());
        UserEntity foundUser = userRepository.findByAccountNumber(balanceEnquiry.getAccountNumber());
        logger.info("check data: " + foundUser);
        BankResponse bankResponse = new BankResponse();
        if(foundUser == null){
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE);
            bankResponse.setAccountInfo(null);

            return bankResponse;
        }else{
            //UserEntity foundUser = userRepository.findByAccountNumber(enquiry.getAccountNumber());
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_FOUND_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS);
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountBalance(foundUser.getAccountBalance());
            accountInfo.setAccountNumber(balanceEnquiry.getAccountNumber());
            accountInfo.setAccountName(foundUser.getFirstname() + " " + foundUser.getLastname() + " " + foundUser.getOthername());
            bankResponse.setAccountInfo(accountInfo);
            return bankResponse;

//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
//                    .accountInfo(AccountInfo.builder()
//                            .accountBalance(foundUser.getAccountBalance())
//                            .accountNumber(enquiry.getAccountNumber())
//                            .accountName(foundUser.getFirstname() + " " + foundUser.getLastname() + " " + foundUser.getOthername())
//                            .build())
//                    .build();
        }
    }

    @Override
    public String nameEnquiry(BalanceEnquiry balanceEnquiry) {
        boolean isAccountExists = userRepository.existsByAccountNumber(balanceEnquiry.getAccountNumber());
        logger.info("check data:" + isAccountExists);
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
        UserEntity foundUser = userRepository.findByAccountNumber(balanceEnquiry.getAccountNumber());
        return foundUser.getFirstname() + " " + foundUser.getLastname() + " " + foundUser.getOthername();
    }
}
