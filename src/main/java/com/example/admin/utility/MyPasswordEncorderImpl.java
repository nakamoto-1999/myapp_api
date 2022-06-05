package com.example.admin.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyPasswordEncorderImpl implements MyPasswordEncorder{

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public String encord(String password) {
        return passwordEncoder.encode(password);
    }
}
