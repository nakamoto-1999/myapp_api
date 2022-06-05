package com.example.admin.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Integer userId;
    private String name;
    private Boolean isValid;
    private Timestamp createAt;
    private Timestamp updatedAt;

}

