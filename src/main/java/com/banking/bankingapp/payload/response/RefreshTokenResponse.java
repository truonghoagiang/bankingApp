package com.banking.bankingapp.payload.response;

import com.banking.bankingapp.dto.RefreshTokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {

    private String message;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

}
