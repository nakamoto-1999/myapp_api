package com.example.admin.response;

import com.example.admin.entity.User;
import lombok.Data;

@Data

public class UserResponseAuth extends UserResponse{

    private String email;

    public UserResponseAuth(User user){
        super(user);
        email = user.getEmail();
    }

}
