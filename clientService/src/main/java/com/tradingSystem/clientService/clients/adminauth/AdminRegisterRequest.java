package com.tradingSystem.clientService.clients.adminauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
