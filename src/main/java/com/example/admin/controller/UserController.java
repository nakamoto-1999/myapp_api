package com.example.admin.controller;

import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.response.UserResponseAuth;
import com.example.admin.service.ThreadService;
import com.example.admin.utility.UserUtil;
import com.example.admin.service.PostService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    ThreadService threadService;

    @Autowired
    UserUtil securityUtil;

    @PostMapping("/user/create")
    public ResponseEntity<?> createGeneralUser(@Validated @RequestBody UserCreateUpdateRequest request){
        userService.createGeneralUser(request);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/auth/admin/user/create")
    public ResponseEntity<?> createAdminUser(@Validated @RequestBody UserCreateUpdateRequest request){
        userService.createAdminUser(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/auth/admin/user")
    //一般ユーザーの場合はroleIdが2
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable Long userId){
        UserResponse response = userService.getResponseByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/user/{userId}")
    public ResponseEntity<?> getUserByUserIdForAuth(@PathVariable Long userId){
        UserResponseAuth response = userService.getResponseAuthByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("auth/login/user")
    public ResponseEntity<?> getUserByAuth(Authentication auth){
        return ResponseEntity.ok(userService.getResponseByAuth(auth));
    }

    @PutMapping("/auth/user/{userId}/update")
    public ResponseEntity<?> updateByUserId(@PathVariable Long userId ,@Validated @RequestBody UserCreateUpdateRequest request, Authentication auth){
        userService.updateByUserId(userId , auth , request);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/auth/admin/user/{userId}/switch-permit")
    public ResponseEntity<?> validateByUserId(@PathVariable Long userId){
        userService.switchPermitByUserId(userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/auth/user/{userId}/delete")
    public ResponseEntity<?> deleteByUserId(@PathVariable Long userId , Authentication auth){
        userService.deleteByUserId(userId , auth);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/user/is-email-exist/{email}")
    public ResponseEntity<?> isEmailExist(@PathVariable String email , Authentication auth){
        return ResponseEntity.ok(userService.isExistEmail(auth , email));
    }

    @GetMapping("/auth/user/is-email-exist/{email}")
    public ResponseEntity<?> isEmailExistForAuth(@PathVariable String email , Authentication auth){
        return ResponseEntity.ok(userService.isExistEmail(auth , email));
    }

}

