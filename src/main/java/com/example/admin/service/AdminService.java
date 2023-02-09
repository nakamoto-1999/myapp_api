package com.example.admin.service;

import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.AdminResponse;
import com.example.admin.response.AdminResponseAuth;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AdminService {

    AdminResponse createAdminUser(UserCreateUpdateRequest request);
    List<AdminResponse> getAllResponses();
    AdminResponseAuth getResponseAuthByUserId(Long userId);
    AdminResponse getResponseByUserId(Long userId);
    AdminResponse getResponseByAuth(Authentication auth);
    void updateByUserId(Long userId ,Authentication auth ,UserCreateUpdateRequest request);
    //ユーザーの無効化及び有効化
    void deleteByUserId(Long userId , Authentication auth);

    boolean isExistEmail(Authentication auth , String newEmail);

}
