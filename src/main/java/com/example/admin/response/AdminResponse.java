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

    private Long userId;
    private String name;
    private Role role;
    private boolean isPermitted;
    private boolean isDeleted;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public AdminResponse(Admin admin){
        if(admin == null)return;
        this.userId = admin.getAdminId();
        this.name = admin.isDeleted() ? "（削除済みユーザー）" : admin.getName();
        this.isDeleted = admin.isDeleted();
        this.createdAt = admin.getCreatedAt();
        this.updatedAt = admin.getUpdatedAt();
    }

}

