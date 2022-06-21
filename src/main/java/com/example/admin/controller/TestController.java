package com.example.admin.controller;

import com.example.admin.entity.AccessAuthorizationPk;
import com.example.admin.repository.AccessAuthrizationRepository;
import com.example.admin.request.UserAuthForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:3000")
public class TestController {
    @GetMapping("/public/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok(null);
    }
}
