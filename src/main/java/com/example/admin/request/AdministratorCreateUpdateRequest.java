package com.example.admin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AdministratorCreateUpdateRequest {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9 -~]+$")
    private String name;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9 -~]+$")
    @Size(min = 8)
    private String password;

}
