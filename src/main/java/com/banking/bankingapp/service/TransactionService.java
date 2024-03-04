package com.banking.bankingapp.service;

import com.banking.bankingapp.dto.TransactionDTO;
import com.banking.bankingapp.entity.TransactionEntity;
import com.banking.bankingapp.repositoty.TransactionRepository;
import com.banking.bankingapp.service.Imp.TransactionServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements TransactionServiceImp {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAccountNumber(transactionDTO.getAccountNumber());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus("SUCCESS");
        transactionRepository.save(transaction);
    }
}
