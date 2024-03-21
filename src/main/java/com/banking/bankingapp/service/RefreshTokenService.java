package com.banking.bankingapp.service;

import com.banking.bankingapp.dto.RefreshTokenDto;
import com.banking.bankingapp.entity.RefreshTokenEntity;
import com.banking.bankingapp.exception.RefreshTokenException;
import com.banking.bankingapp.repositoty.RefreshTokenRepository;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.RefreshTokenServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements RefreshTokenServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${token.refresh.expieration}")
    private Long jwtExpierationFreshToken;

    @Override
    public RefreshTokenEntity findToken(String token) {

        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity generateRefreshToken(String email) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUsers(userRepository.findByEmail(email));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredate(Instant.now().plusMillis(jwtExpierationFreshToken));

        RefreshTokenEntity savedrefreshToken = refreshTokenRepository.save(refreshToken);

        return savedrefreshToken;

    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshToken) {
        if(refreshToken.getExpiredate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(refreshToken.getToken(),"Refresh Token was expired. Please make a new signin request.");
        }
        return refreshToken;
    }

}
