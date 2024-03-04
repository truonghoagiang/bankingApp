package com.banking.bankingapp.repositoty;

import com.banking.bankingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Boolean existsByEmail(String email);

    //@Query(value = "SELECT * FROM users WHERE 'accountNumber' =?1", nativeQuery = true)
    Boolean existsByAccountNumber (String accountNumber);

    //@Query(value = "SELECT * FROM users WHERE 'accountNumber' =?1", nativeQuery = true)
    UserEntity findByAccountNumber(String accountNumber);


}
