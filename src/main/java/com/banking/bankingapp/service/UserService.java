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
import java.math.BigInteger;
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

        UserEntity user = new UserEntity();
        user.setIdnumber(userDTO.getIdnumber());
        user.setFullname(userDTO.getFullname());
        user.setOthername(userDTO.getOthername());
        user.setGender(userDTO.getGender());
        user.setAddress(userDTO.getAddress());
        user.setStateOfOrigin(userDTO.getStateOfOrigin());
        user.setEmail(userDTO.getEmail());
        user.setPhonenumber(userDTO.getPhonenumber());
        user.setStatus("ACTIVE");
        user.setAccountNumber(AccountUtils.generateAccountNumber());
        user.setAccountBalance(BigDecimal.ZERO);
        logger.info("check accountNumber: " + user.getAccountNumber());
        UserEntity saveUser = userRepository.save(user);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation! Your account has been successfully created. \nYour Account Details: \n" +
                        "Account Name: " + saveUser.getFullname() + "\nAccount Number: " + saveUser.getAccountNumber())
                .build();
        emailServiceImp.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFullname())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry) {
        //check if the provided number exists in the db
        boolean isAccountExists = userRepository.existsByAccountNumber(balanceEnquiry.getAccountNumber());
        BankResponse bankResponse = new BankResponse();
        if(!isAccountExists){
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE);
            bankResponse.setAccountInfo(null);
        }else{
            UserEntity foundUser = userRepository.findByAccountNumber(balanceEnquiry.getAccountNumber());
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_FOUND_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS);
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountBalance(foundUser.getAccountBalance());
            accountInfo.setAccountNumber(balanceEnquiry.getAccountNumber());
            accountInfo.setAccountName(foundUser.getFullname());
            bankResponse.setAccountInfo(accountInfo);
        }
        return bankResponse;
    }

    @Override
    public String nameEnquiry(BalanceEnquiry balanceEnquiry) {
        boolean isAccountExists = userRepository.existsByAccountNumber(balanceEnquiry.getAccountNumber());
        logger.info("check data:" + isAccountExists);
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }else {
            UserEntity foundUser = userRepository.findByAccountNumber(balanceEnquiry.getAccountNumber());
            return foundUser.getFullname();
        }
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        //checking ìf the account exists

        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        BankResponse bankResponse = new BankResponse();
        if(!isAccountExists){
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE);
            bankResponse.setAccountInfo(null);
        }else{
            UserEntity usertoCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
            usertoCredit.setAccountBalance(usertoCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
            userRepository.save(usertoCredit);
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE);

            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountName(usertoCredit.getFullname());
            accountInfo.setAccountNumber(creditDebitRequest.getAccountNumber());
            accountInfo.setAccountBalance(usertoCredit.getAccountBalance());
            bankResponse.setAccountInfo(accountInfo);
        }
    return bankResponse;
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        //check if account exists
        //check if the amount you intent to withdraw is not larger than the current amount
        boolean isAccountExists = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        BankResponse bankResponse = new BankResponse();
        if(!isAccountExists){
            bankResponse.setResponseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE);
            bankResponse.setResponseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE);
            bankResponse.setAccountInfo(null);
        }
        else {
            UserEntity usertoDedit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
            BigInteger availableBalance = usertoDedit.getAccountBalance().toBigInteger();
            BigInteger debitAmount = creditDebitRequest.getAmount().toBigInteger();
            if(availableBalance.intValue() < debitAmount.intValue()){
                bankResponse.setResponseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE);
                bankResponse.setResponseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE);
                bankResponse.setAccountInfo(null);
            }
            else{
                usertoDedit.setAccountBalance(usertoDedit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
                userRepository.save(usertoDedit);

                bankResponse.setResponseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE);
                bankResponse.setResponseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE);
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setAccountNumber(creditDebitRequest.getAccountNumber());
                accountInfo.setAccountName(usertoDedit.getFullname());
                accountInfo.setAccountBalance(usertoDedit.getAccountBalance());
                bankResponse.setAccountInfo(accountInfo);
            }
        }
        return bankResponse;
    }
}
