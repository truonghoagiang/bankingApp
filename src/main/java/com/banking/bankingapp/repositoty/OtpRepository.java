package com.banking.bankingapp.repositoty;

import com.banking.bankingapp.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<OtpEntity,Long> {
    OtpEntity findByEmail(String email);
}
