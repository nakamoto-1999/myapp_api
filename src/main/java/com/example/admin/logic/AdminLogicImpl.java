package com.example.admin.logic;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import com.example.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminLogicImpl implements AdminLogic {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin getEntityByAdminId(Long userId) {
        Admin admin = adminRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException());
        return admin;
    }
}
