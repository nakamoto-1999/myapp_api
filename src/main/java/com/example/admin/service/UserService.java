package com.example.admin.service;

import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import com.example.admin.response.UserResponse;

import java.util.List;

public interface UserService {

    void create(String name , String email , String password , Integer roleId);
    List<UserResponse> getAllResponses();
    UserResponse getResponseByUserId(Integer userId);
    void update(Integer userId , String name , String email , String password);
    //ユーザーの無効化及び有効化
    void validate(Integer userId);
    void invalidate(Integer userId);
    void delete(Integer userId);

}
