package com.banking.bankingapp.payload.response;

import com.banking.bankingapp.dto.RefreshTokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private int statuscode;
    private String message;
    private String jwtToken;
    private String type;
    private String email;
    private RefreshTokenDto refreshtoken;
    private List<String> role;

}
