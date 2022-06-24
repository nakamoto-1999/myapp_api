package com.example.admin.controller;

import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.service.ThreadService;
import com.example.admin.utility.SecurityUtil;
import com.example.admin.service.PostService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    ThreadService threadService;

    @Autowired
    SecurityUtil securityUtil;

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

    @GetMapping("/auth/admin/user/all")
    //一般ユーザーの場合はroleIdが2
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Long userId){
        UserResponse response = userService.getResponseByUserId(userId);
        response.setThreads(threadService.getAllResponseByUserId(userId));
        response.setPosts(postService.getAllResponsesByUserId(userId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/auth/user/{userId}/update")
    public ResponseEntity<?> updateByUserId(@PathVariable Long userId ,@Validated @RequestBody UserCreateUpdateRequest request, Authentication auth){
        userService.updateByUserId(userId , auth , request);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/auth/admin/user/{userId}/validate")
    public ResponseEntity<?> validateByUserId(@PathVariable Long userId){
        userService.validateByUserId(userId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/auth/admin/user/{userId}/invalidate")
    public ResponseEntity<?> invalidateByUserId(@PathVariable Long userId){
        userService.invalidateByUserId(userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/auth/user/{userId}/delete")
    public ResponseEntity<?> deleteByUserId(@PathVariable Long userId , Authentication auth){
        userService.deleteByUserId(userId , auth);
        return ResponseEntity.ok(null);
    }

}

