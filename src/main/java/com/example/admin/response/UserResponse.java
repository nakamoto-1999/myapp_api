package com.example.admin.response;


import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserResponse {

    private Integer userId;
    private String name;
    private Role role;
    private Boolean isValid;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserResponse(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.role = user.getRole();
        this.isValid = user.getIsValid();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}

