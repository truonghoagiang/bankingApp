package com.banking.bankingapp.controller;


import com.banking.bankingapp.dto.*;

import com.banking.bankingapp.payload.request.LoginRequest;
import com.banking.bankingapp.service.Imp.RefreshTokenServiceImp;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private RefreshTokenServiceImp refreshTokenServiceImp;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(
            summary = "Create new User Account",
            description = "creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/signup")
    public ResponseEntity<?> createAccount(@RequestBody UserDTO userDTO){

        return userServiceImp.createAccount(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest){
        return userServiceImp.signin(loginRequest);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenDto request){
        return refreshTokenServiceImp.refreshtoken(request);
    }
}
