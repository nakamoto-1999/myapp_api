package com.example.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin("http://localhost:3000")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<?>test(){
        return ResponseEntity.ok(null);
    }
}
