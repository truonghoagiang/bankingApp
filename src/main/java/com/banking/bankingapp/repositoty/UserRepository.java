package com.banking.bankingapp.repositoty;

import com.banking.bankingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber (String accountNumber);
    UserEntity findByAccountNumber(String accountNumber);
    UserEntity findByEmail(String username);



}
