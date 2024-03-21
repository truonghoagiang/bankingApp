package com.banking.bankingapp.service;

import com.banking.bankingapp.config.JwtTokenProvider;
import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.entity.RefreshTokenEntity;
import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.payload.request.LoginRequest;
import com.banking.bankingapp.payload.response.BankResponse;
import com.banking.bankingapp.payload.response.JwtResponse;
import com.banking.bankingapp.payload.response.RefreshTokenResponse;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.EmailServiceImp;
import com.banking.bankingapp.service.Imp.RefreshTokenServiceImp;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import com.banking.bankingapp.utils.AccountUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailServiceImp emailServiceImp;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenServiceImp refreshTokenServiceImp;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    Gson gson = new Gson();

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public ResponseEntity<?> createAccount(UserDTO userDTO) {
        /**
         * creating an account - save it to db
         * check if user already had an account
         */
        if(userRepository.existsByEmail(userDTO.getEmail())){
            BankResponse bankResponse = new BankResponse();
            bankResponse.setResponseCode(HttpStatus.BAD_REQUEST.toString());
            bankResponse.setResponseMessage("Email has already taken!");
            bankResponse.setAccountInfo(null);
            return new ResponseEntity(bankResponse, HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setIdnumber(userDTO.getIdnumber());
        user.setFullname(userDTO.getFullname());
        user.setOthername(userDTO.getOthername());
        user.setGender(userDTO.getGender());
        user.setAddress(userDTO.getAddress());
        user.setStateOfOrigin(userDTO.getStateOfOrigin());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPhonenumber(userDTO.getPhonenumber());
        user.setStatus("ACTIVE");
        user.setAccountNumber(AccountUtils.generateAccountNumber());
        user.setAccountBalance(BigDecimal.ZERO);
        UserEntity saveUser = userRepository.save(user);
        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulation! Your account has been successfully created. \nYour Account Details: \n" +
                        "Account Name: " + saveUser.getFullname() + "\nAccount Number: " + saveUser.getAccountNumber())
                .build();
        emailServiceImp.sendEmailAlert(emailDetails);
        BankResponse bankResponse = new BankResponse();
        bankResponse.setResponseCode(HttpStatus.CREATED.toString());
        bankResponse.setResponseMessage("Account registered suscessfully");

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAccountName(saveUser.getFullname());
        accountInfo.setAccountNumber(saveUser.getAccountNumber());
        accountInfo.setAccountBalance(saveUser.getAccountBalance());

        bankResponse.setAccountInfo(accountInfo);

        return new ResponseEntity(bankResponse, HttpStatus.OK);
    }



    public ResponseEntity<?> signin(LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        logger.info("Check authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String json = gson.toJson(authentication.getAuthorities());
        String jwtToken = jwtTokenProvider.generateToken(json);

        RefreshTokenEntity refreshToken = refreshTokenServiceImp.generateRefreshToken(username);
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setToken(refreshToken.getToken());
        refreshTokenDto.setExpiredate(refreshToken.getExpiredate());
        refreshTokenDto.setIduser(refreshToken.getUsers().getId());

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setStatuscode(HttpStatus.OK.value());
        jwtResponse.setMessage("Login successfull");
        jwtResponse.setType("Bearer");
        jwtResponse.setJwtToken(jwtToken);
        jwtResponse.setEmail((String) authentication.getPrincipal());

        List<String> listRoles = authentication.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        jwtResponse.setRole(listRoles);
        jwtResponse.setRefreshtoken(refreshTokenDto);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
