package com.banking.bankingapp.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;


@RestController
@RequestMapping("/secretkey")
public class SecretKeyController {

    @GetMapping
    public String generateSecretkey(){
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        String strSecretKey = Encoders.BASE64.encode(secretKey.getEncoded());
        return "Secret Key: " + strSecretKey;
    }
}
