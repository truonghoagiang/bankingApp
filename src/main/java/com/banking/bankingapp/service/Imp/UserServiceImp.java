package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceImp {
    BankResponse createAccount(UserDTO userDTO);
    BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry);
    String nameEnquiry(BalanceEnquiry balanceEnquiry);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
