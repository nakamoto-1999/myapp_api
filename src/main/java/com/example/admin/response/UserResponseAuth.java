package com.example.admin.response;

import com.example.admin.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResponseAuth extends UserResponse{

    private String email;

    public UserResponseAuth(User user){
        super(user);
        if(user == null)return;
        email = user.getEmail();
    }

}
