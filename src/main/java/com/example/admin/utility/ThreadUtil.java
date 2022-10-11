package com.example.admin.utility;


import com.example.admin.entity.BlockedUserOfThread;
import com.example.admin.entity.Thread;

import java.util.List;

public interface ThreadUtil {

    boolean isStopped(Thread thread);
    boolean isUserIdExistInBlockedList(List<BlockedUserOfThread> blockedUsers , Long userId);

}
