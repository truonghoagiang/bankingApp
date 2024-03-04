package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.dto.BalanceEnquiry;
import com.banking.bankingapp.dto.BankResponse;
import com.banking.bankingapp.dto.CreditDebitRequest;
import com.banking.bankingapp.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserServiceImp {
    BankResponse createAccount(UserDTO userDTO);
    BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry);
    String nameEnquiry(BalanceEnquiry balanceEnquiry);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);

}
