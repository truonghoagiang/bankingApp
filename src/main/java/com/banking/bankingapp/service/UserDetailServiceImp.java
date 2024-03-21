package com.banking.bankingapp.service;

import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.repositoty.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            UserEntity user = userRepository.findByEmail(email);
            return UserDetailsImp.createUserDetail(user);
        } catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("User not found with email:" + email);
        }
    }
}
