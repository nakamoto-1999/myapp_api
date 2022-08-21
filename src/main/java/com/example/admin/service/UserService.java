package com.example.admin.service;

import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.response.UserResponseAuth;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserResponse createGeneralUser(UserCreateUpdateRequest request);
    UserResponse createAdminUser(UserCreateUpdateRequest request);
    List<UserResponse> getAllResponses();
    UserResponseAuth getResponseAuthByUserId(Long userId);
    UserResponse getResponseByUserId(Long userId);
    UserResponse getResponseByAuth(Authentication auth);
    void updateByUserId(Long userId ,Authentication auth ,UserCreateUpdateRequest request);
    //ユーザーの無効化及び有効化
    void switchPermitByUserId(Long userId);
    void deleteByUserId(Long userId , Authentication auth);

    boolean isExistEmail(Authentication auth , String newEmail);

}
