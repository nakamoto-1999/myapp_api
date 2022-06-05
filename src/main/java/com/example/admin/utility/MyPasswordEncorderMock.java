package com.example.admin.utility;

import org.springframework.stereotype.Component;

@Component
public class MyPasswordEncorderMock implements MyPasswordEncorder{

    @Override
    public String encord(String password) {
        return password;
    }
}
