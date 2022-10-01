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

        if(thread == null)return false;

        //スレッドのレス数
        final Integer postNum = thread.getPosts().size();

        //スレッドのレス数が1000を超える場合
        if(postNum >= 1000)return true;

        //以下時間経過によるスレッドストッパー
        final Long duraMin = 1L;

        //レス数が0の場合、スレッドが立てられた時刻と現在時刻を比較して、duraMin分経過していた場合
        if(postNum <= 0) {
            final LocalDateTime threadCreatedAt = thread.getCreatedAt().toLocalDateTime();
            return Duration.between( threadCreatedAt , LocalDateTime.now() ).toMinutes() >= duraMin;
        }
        //レス数が0でない場合、一番新しいレスが投稿された時刻と現在時刻を比較して、duraMin分経過していた場合
        else{
            //最も新しいレスの投稿時間を取得
            final LocalDateTime latestPostCreatedAt = thread.getPosts().get(postNum - 1)
                    .getCreatedAt().toLocalDateTime();
            //System.out.println(thread.getPosts().get(postNum - 1).getContent());
            //System.out.println(Duration.between( latestPostCreatedAt , LocalDateTime.now() ).toMinutes());
            return Duration.between( latestPostCreatedAt ,LocalDateTime.now() ).toMinutes() >= duraMin;
        }

    }


}
