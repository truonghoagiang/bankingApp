package com.banking.bankingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {

    @Schema(name = "User account name")
    private String accountName;
    @Schema(name = "Balance")
    private BigDecimal accountBalance;
    @Schema(name = "Account Number")
    private String accountNumber;

}
