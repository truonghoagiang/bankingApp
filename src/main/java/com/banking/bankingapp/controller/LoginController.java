package com.banking.bankingapp.controller;

import com.banking.bankingapp.config.JwtTokenProvider;
import com.banking.bankingapp.dto.BasicResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private Gson gson = new Gson();

    @PostMapping("/sigin")
    public ResponseEntity<?> signin(@RequestParam String username, @RequestParam String password){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authentication = authenticationManager.authenticate(token);
        String json = gson.toJson(authentication.getAuthorities());
        String jwtToken = jwtTokenProvider.generateToken(json);
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setStatusCode(HttpStatus.ACCEPTED.value());
        basicResponse.setMessage("Login Successfull");
        basicResponse.setData(jwtToken);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }
}
