package com.banking.bankingapp.exception;

import com.banking.bankingapp.payload.response.RefreshTokenResponse;
import com.banking.bankingapp.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RefreshTokenException extends RuntimeException{
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handlerException(){
        RefreshTokenResponse response  =new RefreshTokenResponse();
        response.setAccessToken(null);
        response.setTokenType(null);
        response.setRefreshToken("Token is not exists");

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
