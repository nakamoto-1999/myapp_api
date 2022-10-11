package com.example.admin.utility;

import com.example.admin.entity.Thread;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ThreadStopperUtilImpl implements ThreadStopperUtil{

    @Override
    public boolean isStopped(Thread thread){

        if(thread == null)throw new RuntimeException("required variables is null!");

        //スレッドのレス数が1000を超える場合
        final Integer postNum = thread.getPosts().size();
        if(postNum >= 1000)return true;

        //スレッドが立てられた時刻と現在時刻を比較して、duraMin分経過していた場合
        final LocalDateTime threadCreatedAt = thread.getCreatedAt().toLocalDateTime();
        return Duration.between( threadCreatedAt , LocalDateTime.now() ).toHours() >= 12;

    }


}
