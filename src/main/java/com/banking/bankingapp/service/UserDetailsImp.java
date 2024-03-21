package com.banking.bankingapp.service;

import com.banking.bankingapp.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDetailsImp implements UserDetails {

    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImp(String email, String password, Collection<? extends GrantedAuthority> authorities){
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImp createUserDetail(UserEntity user){
        List<GrantedAuthority> listRole = new ArrayList<>();
        SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(user.getRoles().getName());
        listRole.add(authorities);
        return new UserDetailsImp(user.getEmail(),user.getPassword(), listRole);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
