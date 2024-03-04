package com.banking.bankingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String idnumber;
    private String fullname;
    private String othername;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String email;
    private String phonenumber;
    private String status;
}
