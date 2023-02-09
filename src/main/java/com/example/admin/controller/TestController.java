package com.example.admin.controller;

import com.example.admin.repository.RoleRepository;
import com.example.admin.repository.AdminRepository;
import com.example.admin.utility.TimestampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    AdminRepository uRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TimestampUtil timestampUtil;

    @GetMapping("/")
    public ResponseEntity<?> test(){

        return ResponseEntity.ok("Hello Soshou Gokko");
    }
}
