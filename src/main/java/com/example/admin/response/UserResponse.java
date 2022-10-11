package com.example.admin.response;


import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class UserResponse {

    private Long userId;
    private String name;
    private Role role;
    private boolean isPermitted;
    private boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserResponse(User user){
        if(user == null)return;
        this.userId = user.getUserId();
        this.name = user.getName();
        this.role = user.getRole();
        this.isPermitted = user.isPermitted();
        this.isDeleted = user.isDeleted();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}

