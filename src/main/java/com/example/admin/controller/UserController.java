package com.example.admin.controller;

import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/auth/user")
    ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/auth/user/{userId}")
    ResponseEntity<?> getUserByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getResponseByUserId(userId));
    }

    @PutMapping("/auth/user/{userId}/switch-permission")
    ResponseEntity<?> switchPermissionByUserId(Long userId){
        userService.switchPermission(userId);
        return ResponseEntity.ok(null);
    }

}
