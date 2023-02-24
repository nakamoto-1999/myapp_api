package com.example.admin.controller;

import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.AdminResponse;
import com.example.admin.service.ThreadService;
import com.example.admin.utility.UserUtil;
import com.example.admin.service.PostService;
import com.example.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    AdminService userService;

    @Autowired
    PostService postService;

    @Autowired
    ThreadService threadService;

    @Autowired
    UserUtil securityUtil;


    @PostMapping("/auth/admin/create")
    public ResponseEntity<?> createAdminUser(@Validated @RequestBody UserCreateUpdateRequest request){
        userService.createAdmin(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/auth/admin")
    public ResponseEntity<?> getAllAdmins(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/auth/admin/{adminId}")
    public ResponseEntity<?> getAdminByAdminId(@PathVariable Long adminId){
        AdminResponse response = userService.getResponseByAdminId(adminId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/auth/login/current")
    public ResponseEntity<?> getUserByAuth(Authentication auth){
        return ResponseEntity.ok(userService.getResponseByAuth(auth));
    }

    @PutMapping("/auth/admin/{adminId}/update")
    public ResponseEntity<?> updateByAdminId(@PathVariable Long adminId ,@Validated @RequestBody UserCreateUpdateRequest request, Authentication auth){
        userService.updateByAdminId(adminId , auth , request);
        return ResponseEntity.ok(null);
    }


    @DeleteMapping("/auth/admin/{adminId}/delete")
    public ResponseEntity<?> deleteByAdminId(@PathVariable Long adminId , Authentication auth){
        userService.deleteByAdminId(adminId , auth);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/auth/is-email-exist/{email}")
    public ResponseEntity<?> isEmailExist(@PathVariable String email , Authentication auth){
        return ResponseEntity.ok(userService.isExistEmail(auth , email));
    }

    @GetMapping("/auth/admin/is-email-exist/{email}")
    public ResponseEntity<?> isEmailExistForAuth(@PathVariable String email , Authentication auth){
        return ResponseEntity.ok(userService.isExistEmail(auth , email));
    }

}

