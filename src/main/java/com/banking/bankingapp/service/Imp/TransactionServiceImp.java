package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.dto.TransactionDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransactionServiceImp {
    void saveTransaction(TransactionDTO transactionDTO);
}
