package com.banking.bankingapp.service.Imp;

import com.banking.bankingapp.dto.BalanceEnquiry;
import com.banking.bankingapp.payload.response.BankResponse;
import com.banking.bankingapp.payload.request.CreditDebitRequest;
import com.banking.bankingapp.payload.request.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public interface BankServiceImp {
    String nameEnquiry(BalanceEnquiry balanceEnquiry);
    BankResponse balanceEnquiry(BalanceEnquiry balanceEnquiry);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
