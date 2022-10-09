package com.example.admin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadCreateRequest {

    @NotEmpty
    private String overview;

    @NotEmpty
    private String point;

    @NotEmpty
    private String red;

    @NotEmpty
    private String blue;

}
