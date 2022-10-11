package com.example.admin.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ThreadConcludeRequest {

    @NotNull
    Long concludedColorId;

    @NotEmpty
    String conclusionReason;

}
