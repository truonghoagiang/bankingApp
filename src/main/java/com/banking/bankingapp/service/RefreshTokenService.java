package com.banking.bankingapp.service;

import com.banking.bankingapp.config.JwtTokenProvider;
import com.banking.bankingapp.dto.RefreshTokenDto;
import com.banking.bankingapp.entity.RefreshTokenEntity;
import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.exception.RefreshTokenException;
import com.banking.bankingapp.payload.response.RefreshTokenResponse;
import com.banking.bankingapp.repositoty.RefreshTokenRepository;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.RefreshTokenServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements RefreshTokenServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${token.refresh.expieration}")
    private Long jwtExpierationRefreshToken;

    Logger logger = LoggerFactory.getLogger(RefreshTokenException.class);

    @Override
    public RefreshTokenEntity findToken(String token) {

        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity generateRefreshToken(String email) {
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUsers(userRepository.findByEmail(email));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredate(LocalDateTime.now().plusDays(5));

        logger.info("Check expired date: " + refreshToken.getExpiredate());

        RefreshTokenEntity savedrefreshToken = refreshTokenRepository.save(refreshToken);

        return savedrefreshToken;

    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity refreshToken) {
        if(refreshToken.getExpiredate().compareTo(LocalDateTime.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            //throw new RefreshTokenException(refreshToken.getToken(),"Refresh Token was expired. Please make a new signin request.");
        }
        return refreshToken;
    }

    public ResponseEntity<?> refreshtoken(RefreshTokenDto request){
        String refreshtokenrequest = request.getToken();
        RefreshTokenResponse response = new RefreshTokenResponse();
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshtokenrequest);
        if(refreshTokenEntity != null && request.getExpiredate().isAfter(LocalDateTime.now())){
            Optional<UserEntity> user = userRepository.findById(request.getIduser());
            String token = jwtTokenProvider.generateToken(user.map(item -> item.getEmail()).toString());

            RefreshTokenEntity newtoken = new RefreshTokenEntity();
            newtoken.setToken(UUID.randomUUID().toString());
            newtoken.setExpiredate(request.getExpiredate());
            newtoken.setUsers(user.get());
            refreshTokenRepository.deleteById(request.getId());
            refreshTokenRepository.save(newtoken);

            response.setMessage("Created new access token and refresh token successfull");
            response.setAccessToken(token);
            response.setRefreshToken(newtoken.getToken());
        }else{
            response.setMessage("Token is expired or not exists. Please make a new Login request!");
            response.setAccessToken(null);
            response.setRefreshToken(null);
            response.setTokenType(null);
        }

        return new ResponseEntity(response, HttpStatus.OK);

    }
}
