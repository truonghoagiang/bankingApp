package com.banking.bankingapp.config;

import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.service.Imp.LoginServiceImp;
import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomAuthenProvider implements AuthenticationProvider {

    @Autowired
    private LoginServiceImp loginServiceImp;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserEntity user = loginServiceImp.checkLogin(username, password);
        if(user != null){
            List<GrantedAuthority> listRole = new ArrayList<>();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRoles().getName());
            listRole.add(authority);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,"",listRole);
            log.info("Check token: " + authenticationToken);
            return authenticationToken;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals((UsernamePasswordAuthenticationToken.class));
    }
}
