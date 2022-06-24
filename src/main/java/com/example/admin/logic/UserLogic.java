package com.example.admin.logic;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;

public interface UserLogic {

    User getEntitiyByUserId(Long userId);

}
