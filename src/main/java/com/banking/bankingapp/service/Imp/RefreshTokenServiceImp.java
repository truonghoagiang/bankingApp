package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.dto.RefreshTokenDto;
import com.banking.bankingapp.entity.RefreshTokenEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RefreshTokenServiceImp {
    RefreshTokenEntity findToken(String token);
    RefreshTokenEntity generateRefreshToken(String email);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshToken);
    ResponseEntity<?> refreshtoken(RefreshTokenDto request);

}
