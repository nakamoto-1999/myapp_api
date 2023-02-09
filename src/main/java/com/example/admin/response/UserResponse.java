package com.example.admin.response;

import com.example.admin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class UserResponse {

    Long userId;
    Timestamp createdAt;
    Timestamp updatedAt;

    public UserResponse(User user){
        userId = user.getUserId();
        createdAt = user.getCreatedAt();
        updatedAt = user.getUpdatedAt();
    }

}
