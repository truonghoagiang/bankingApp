package com.banking.bankingapp.repositoty;

import com.banking.bankingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber (String accountNumber);
    UserEntity findByAccountNumber(String accountNumber);


}
