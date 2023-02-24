package com.example.admin.service;

import com.example.admin.entity.User;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.UserRepository;
import com.example.admin.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserLogic userLogic;

    @Override
    public List<UserResponse> getAllResponses() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach(user -> {
            userResponses.add(new UserResponse(user));
        });
        return userResponses;
    }

    @Override
    public UserResponse getResponseByUserId(Long userId) {
        UserResponse response = new UserResponse(userLogic.getEntityByUserId(userId));
        return response;
    }

    @Override
    public void switchPermission(Long userId) {
        User user = userLogic.getEntityByUserId(userId);
        user.setPermitted(!user.isPermitted());
        userRepository.save(user);
    }

}
