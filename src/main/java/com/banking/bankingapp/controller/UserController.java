package com.banking.bankingapp.controller;

import com.banking.bankingapp.dto.BalanceEnquiry;
import com.banking.bankingapp.dto.BankResponse;
import com.banking.bankingapp.dto.UserDTO;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceImp userServiceImp;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserDTO userDTO){
        return userServiceImp.createAccount(userDTO);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody BalanceEnquiry balanceEnquiry){
        return userServiceImp.balanceEnquiry(balanceEnquiry);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody BalanceEnquiry balanceEnquiry){
        return userServiceImp.nameEnquiry(balanceEnquiry);
    }
}
