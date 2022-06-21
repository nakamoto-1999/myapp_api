package com.example.admin.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserCreateUpdateRequest {

    @NotEmpty
    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

}
