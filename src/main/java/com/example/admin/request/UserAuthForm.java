package com.example.admin.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthRequest {

        String email;
        String password;

}


