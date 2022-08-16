package com.example.admin.utility;

import org.springframework.stereotype.Component;


import java.sql.Timestamp;

@Component
public class TimestampUtilImpl implements TimestampUtil{

    public Timestamp getNow(){
        return new Timestamp(System.currentTimeMillis());
    }

}
