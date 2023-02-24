package com.example.admin.response;


import com.example.admin.entity.Admin;
import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class AdminResponse {

    private Long adminId;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AdminResponse(Admin admin){
        if(admin == null)return;
        adminId = admin.getAdminId();
        name = admin.getName();
        email = admin.getEmail();
        createdAt = admin.getCreatedAt();
        updatedAt = admin.getUpdatedAt();
    }

}

