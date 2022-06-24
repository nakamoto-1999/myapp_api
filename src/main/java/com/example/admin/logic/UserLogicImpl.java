package com.example.admin.logic;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLogicImpl implements UserLogic{

    @Autowired
    UserRepository userRepository;

    @Override
    public User getEntitiyByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException());
        return user;
    }
}
