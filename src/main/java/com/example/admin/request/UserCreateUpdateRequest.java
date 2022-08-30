package com.example.admin.request;


import com.example.admin.validation.Unique;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserCreateUpdateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9]{1}[A-Za-z0-9_.-]*@{1}[A-Za-z0-9_.-]*.[A-Za-z0-9]*$")
    @Unique
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9!-/:-@\\[-`\\{-\\~]+$")
    @Size(min = 8)
    private String password;

}
