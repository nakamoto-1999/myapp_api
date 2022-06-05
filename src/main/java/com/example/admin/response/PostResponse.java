package com.example.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
public class PostResponse {

    private Integer postId;
    private String ip;
    private UserResponse user;
    private String content;
    private Boolean isValid;
    private Timestamp created_at;
    private Timestamp updated_at;

}
