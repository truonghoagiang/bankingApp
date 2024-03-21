package com.banking.bankingapp.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
