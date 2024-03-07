package com.banking.bankingapp.service;

import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginServiceImp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserEntity checkLogin(String username, String password) {
        UserEntity user = userRepository.findByEmail(username);
        if(user!=null){
            if(passwordEncoder.matches(password,user.getPassword())){
                return user;
            }
        }
        return null;
    }
}
