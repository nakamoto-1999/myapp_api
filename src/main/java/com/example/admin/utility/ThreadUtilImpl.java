package com.example.admin.utility;

import com.example.admin.Constant;
import com.example.admin.entity.BlockedUserOfThread;
import com.example.admin.entity.Thread;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ThreadUtilImpl implements ThreadUtil {

    @Override
    public boolean isStopped(Thread thread){

        if(thread == null)throw new RuntimeException("required variables is null!");

        //スレッドのレス数が1000を超える場合
        final Integer postNum = thread.getPosts().size();
        if(postNum >= 1000)return true;

        //スレッドが立てられた時刻と現在時刻を比較して、duraMin分経過していた場合
        final LocalDateTime threadCreatedAt = thread.getCreatedAt().toLocalDateTime();
        return Duration.between( LocalDateTime.now() , threadCreatedAt ).toDays()
                >= Constant.THREAD_LIFESPAN_DAYS;

    }

    @Override
    public boolean isUserIdExistInBlockedList(List<BlockedUserOfThread> blockedUsers, Long userId) {
        int firstIndex = 0;
        int lastIndex = blockedUsers.size() - 1;
        while(lastIndex - firstIndex >= 0){
            int midIndex = (firstIndex + lastIndex )/ 2;
            if(blockedUsers.get(midIndex).getUser().getUserId() == userId)
                return true;
            else if(blockedUsers.get(midIndex).getUser().getUserId()  < userId)
                firstIndex = midIndex + 1;
            else
                lastIndex = midIndex - 1;
        }
        return false;
    }
}
