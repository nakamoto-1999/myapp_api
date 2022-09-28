package com.example.admin.utility;


import com.example.admin.entity.Thread;

public interface ThreadStopperUtil {
    boolean isThreadExpired(Thread thread);

}
