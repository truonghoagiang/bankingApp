package com.banking.bankingapp.controller;

import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Operation(
            summary = "Create new User Account",
            description = "creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserDTO userDTO){
        return userServiceImp.createAccount(userDTO);
    }


    @Operation(
            summary = "Balance Equiry",
            description = "given an account number to check the account balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 SUCCESS"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody BalanceEnquiry balanceEnquiry){
        return userServiceImp.balanceEnquiry(balanceEnquiry);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody BalanceEnquiry balanceEnquiry){
        return userServiceImp.nameEnquiry(balanceEnquiry);
    }

    @PostMapping("/credit")
    public BankResponse createAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userServiceImp.creditAccount(creditDebitRequest);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return userServiceImp.debitAccount(creditDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transferRequest(@RequestBody TransferRequest transferRequest){
        return userServiceImp.transfer(transferRequest);
    }
}
