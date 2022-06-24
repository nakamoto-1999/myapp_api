package com.example.admin.response;


import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Data
public class UserResponse {

    private Long userId;
    private String name;
    private Role role;
    private boolean isValid;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<ThreadResponse> threads;
    private List<PostResponse> posts;

    public UserResponse(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.role = user.getRole();
        this.isValid = user.isValid();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}

