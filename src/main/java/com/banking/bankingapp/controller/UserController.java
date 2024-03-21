package com.banking.bankingapp.controller;

import com.banking.bankingapp.config.JwtTokenProvider;
import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.entity.RefreshTokenEntity;
import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.exception.RefreshTokenException;
import com.banking.bankingapp.payload.request.LoginRequest;
import com.banking.bankingapp.payload.response.BankResponse;
import com.banking.bankingapp.payload.response.BasicResponse;
import com.banking.bankingapp.payload.response.JwtResponse;
import com.banking.bankingapp.payload.response.RefreshTokenResponse;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.RefreshTokenServiceImp;
import com.banking.bankingapp.service.Imp.UserServiceImp;
import com.banking.bankingapp.service.UserDetailsImp;
import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenServiceImp refreshTokenServiceImp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Gson gson = new Gson();

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
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
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

        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody RefreshTokenDto request){
        String refreshtokenrequest = request.getToken();
        RefreshTokenEntity refreshTokenEntity = refreshTokenServiceImp.findToken(refreshtokenrequest);
        if(refreshTokenEntity == null){
            throw new RefreshTokenException("Token is not exists!", refreshtokenrequest);
        }
        if(refreshTokenEntity.getExpiredate().isBefore(Instant.now())){
            throw new RefreshTokenException("Token is expired!", refreshtokenrequest);
        }
        Optional<UserEntity> user = userRepository.findById(request.getIduser());
        String token = jwtTokenProvider.generateToken(user.map(item -> item.getId()).toString());

        RefreshTokenResponse response = new RefreshTokenResponse();
        response.setAccessToken(token);
        response.setRefreshToken(refreshtokenrequest);

        return new ResponseEntity(response, HttpStatus.OK);

    }
}
