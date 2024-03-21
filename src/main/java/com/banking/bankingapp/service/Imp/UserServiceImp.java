package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.dto.*;
import com.banking.bankingapp.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceImp {
    ResponseEntity<?> createAccount(UserDTO userDTO);



}
