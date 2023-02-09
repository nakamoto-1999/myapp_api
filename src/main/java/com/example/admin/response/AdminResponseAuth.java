package com.example.admin.response;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AdminResponseAuth extends AdminResponse {

    private String email;

    public AdminResponseAuth(Admin admin){
        super(admin);
        if(admin == null)return;
        email = admin.getEmail();
    }

}
