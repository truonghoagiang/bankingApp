package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LoginServiceImp {
    UserEntity checkLogin(String username, String password);
}
