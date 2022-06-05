package com.example.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdministratorAuthenticationResponse {

    String jwtToken;
    AdministratorResponse authenticated;

}
