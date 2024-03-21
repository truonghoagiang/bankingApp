package com.banking.bankingapp.controller;

import com.banking.bankingapp.dto.BalanceEnquiry;
import com.banking.bankingapp.payload.response.BankResponse;
import com.banking.bankingapp.payload.request.CreditDebitRequest;
import com.banking.bankingapp.payload.request.TransferRequest;
import com.banking.bankingapp.service.Imp.BankServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banking")
public class BankController {

    @Autowired
    private BankServiceImp bankServiceImp;

    @Operation(
            summary = "Balance Equiry",
            description = "given an account number to check the account balance"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 SUCCESS"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody BalanceEnquiry balanceEnquiry){
        return bankServiceImp.balanceEnquiry(balanceEnquiry);
    }

    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody BalanceEnquiry balanceEnquiry){
        return bankServiceImp.nameEnquiry(balanceEnquiry);
    }
    @PostMapping("/credit")
    public BankResponse createAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return bankServiceImp.creditAccount(creditDebitRequest);
    }

    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest){
        return bankServiceImp.debitAccount(creditDebitRequest);
    }

    @PostMapping("/transfer")
    public BankResponse transferRequest(@RequestBody TransferRequest transferRequest){
        return bankServiceImp.transfer(transferRequest);
    }
}
