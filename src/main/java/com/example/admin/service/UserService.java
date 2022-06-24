package com.example.admin.service;

import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    void createGeneralUser(UserCreateUpdateRequest request);
    void createAdminUser(UserCreateUpdateRequest request);
    List<UserResponse> getAllResponses();
    UserResponse getResponseByUserId(Long userId);
    void updateByUserId(Long userId ,Authentication auth ,UserCreateUpdateRequest request);
    //ユーザーの無効化及び有効化
    void validateByUserId(Long userId);
    void invalidateByUserId(Long userId);
    void deleteByUserId(Long userId , Authentication auth);

}
