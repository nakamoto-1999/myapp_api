package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import com.example.admin.repository.PostRepository;
import com.example.admin.repository.RoleRepository;
import com.example.admin.repository.UserRepository;
import com.example.admin.response.PostResponse;
import com.example.admin.response.UserResponse;
import com.example.admin.utility.MyPasswordEncorder;
import com.example.admin.utility.TimestampManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimestampManager timestampManager;

    @Autowired
    MyPasswordEncorder passwordEncorder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void create(String name, String email, String password , Integer roleId) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(this.passwordEncorder.encord(password));
        user.setIsValid(true);
        user.setRole(roleRepository.getById(roleId));
        //user.setRoleId(roleId);
        user.setCreatedAt(timestampManager.getNow());
        System.out.println(user);
        userRepository.save(user);
    }

    @Override
    public List<UserResponse> getAllResponses() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(User user : users){
            userResponses.add(new UserResponse(user));
        }
        return userResponses;
    }

    @Override
    public UserResponse getResponseByUserId(Integer userId) {
        User user = this.getEntityByUserId(userId);
        return new UserResponse(user);
    }

    private User getEntityByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException());
        return user;
    }

    @Override
    public void update(Integer userId, String name, String email, String password) {
        User user = this.getEntityByUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(
                this.passwordEncorder.encord(password)
        );
        user.setUpdatedAt(timestampManager.getNow());
        userRepository.save(user);
    }

    @Override
    public void validate(Integer userId) {
        User user = this.getEntityByUserId(userId);
        user.setIsValid(true);
        user.setUpdatedAt(timestampManager.getNow());
        userRepository.save(user);
    }

    @Override
    public void invalidate(Integer userId) {
        User user = this.getEntityByUserId(userId);
        user.setIsValid(false);
        user.setUpdatedAt(timestampManager.getNow());
        userRepository.save(user);
    }

    @Override
    public void delete(Integer userId) {
        User user = this.getEntityByUserId(userId);
        userRepository.delete(user);
    }

}
