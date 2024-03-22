package com.banking.bankingapp.repositoty;

import com.banking.bankingapp.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {
    RefreshTokenEntity findByToken(String token);


}
