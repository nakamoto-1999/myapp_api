package com.example.admin.service;

import com.example.admin.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllResponses();
    UserResponse getResponseByUserId(Long userId);
    void switchPermission(Long userId);

}
