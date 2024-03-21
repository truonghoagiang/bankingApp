package com.banking.bankingapp.payload.response;

import com.banking.bankingapp.dto.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private int statusCode;
    private String responseMessage;
    private UserRequest userInfo;
    private OtpResponse otpResponse;
}
