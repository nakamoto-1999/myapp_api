package com.example.admin.controller;

import com.example.admin.security.MyUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
public class TestController {
    @GetMapping("/public/test")
    public ResponseEntity<?> test(Authentication auth){
        MyUserDetails userDetails = null;
        if(auth != null){
            userDetails = (MyUserDetails) auth.getPrincipal();
        };
        return ResponseEntity.ok(userDetails);
    }
}
