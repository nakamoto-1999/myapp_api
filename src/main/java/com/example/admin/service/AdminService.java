package com.example.admin.service;

import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.AdminResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdminService {

    AdminResponse createAdmin(UserCreateUpdateRequest request);
    List<AdminResponse> getAllResponses();
    AdminResponse getResponseByAdminId(Long adminId);
    AdminResponse getResponseByAuth(Authentication auth);
    void updateByAdminId(Long adminId ,Authentication auth ,UserCreateUpdateRequest request);
    //ユーザーの無効化及び有効化
    void deleteByAdminId(Long adminId , Authentication auth);

    boolean isExistEmail(Authentication auth , String newEmail);

}
