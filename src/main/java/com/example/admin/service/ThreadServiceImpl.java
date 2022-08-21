package com.example.admin.service;

import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.logic.ThreadLogic;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.ThreadRepository;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import com.example.admin.utility.TimestampUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService{

    @Autowired
    ThreadRepository threadRepository;

    @Autowired
    TimestampUtil timestampUtil;

    @Autowired
    UserUtil securityUtil;

    @Autowired
    ThreadLogic threadLogic;

    @Autowired
    UserLogic userLogic;

    @Override
    public ThreadResponse createThread(Authentication auth, HttpServletRequest req, ThreadCreateRequest reqBody) {

        if(auth == null ||req == null || reqBody == null ){return new ThreadResponse();}
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        if(userDetails == null){return new ThreadResponse();}
        //許可されていないユーザーの場合は、アクセス拒否
        if(!userDetails.isPermitted()){throw new AccessDeniedException("");}
        Thread thread = new Thread();
        thread.setTitle(reqBody.getTitle());
        User user = userLogic.getEntitiyByUserId(userDetails.getUserId());
        thread.setUser(user);
        thread.setIp(req.getRemoteAddr());
        thread.setValid(true);
        thread.setCreatedAt(timestampUtil.getNow());
        Thread createdThread = threadRepository.save(thread);

        return new ThreadResponse(createdThread);
    }

    @Override
    public List<ThreadResponse> getAllResponses() {
        List<Thread> threads = threadRepository.findAllByOrderByThreadId();
        List<ThreadResponse> threadResponses = new ArrayList<>();
        threads.forEach((Thread thread)->{
            threadResponses.add(new ThreadResponse(thread));
        });
        return threadResponses;
    }

    @Override
    public List<ThreadResponse> getAllResponseByUserId(Long userId) {
        List<Thread> threads = threadRepository.findAllByUserIdOrderByThreadId(userId);
        List<ThreadResponse> threadResponses = new ArrayList<>();
        threads.forEach((Thread thread)->{
            threadResponses.add(new ThreadResponse(thread));
        });
        return threadResponses;
    }

    @Override
    public ThreadResponse getResponseByThreadId(Long threadId) {
        Thread thread = threadLogic.getEntityByThreadId(threadId);
        return new ThreadResponse(thread);
    }

    @Override
    public void validateByThreadId(Long threadId) {
        Thread thread = threadLogic.getEntityByThreadId(threadId);
        //falseの時trueにする
        if(!thread.isValid()) {
            thread.setValid(true);
            thread.setUpdatedAt(timestampUtil.getNow());
            threadRepository.save(thread);
        }
    }

    @Override
    public void invalidateByThreadId(Long threadId) {
        Thread thread = threadLogic.getEntityByThreadId(threadId);
        //trueの時falseにする
        if(thread.isValid()) {
            thread.setValid(false);
            thread.setUpdatedAt(timestampUtil.getNow());
            threadRepository.save(thread);
        }
    }

    @Override
    public void deleteByThreadId(Long threadId , Authentication auth) {

        //IDからスレッドを取得
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //スレッドを立てたユーザーと、認証を受けたユーザーが一致しなければアクセス拒否
        if(auth == null){return;}
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        if(userDetails == null)return;

        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , thread.getUser().getUserId())
                && !securityUtil.isAdmin(userDetails.getAuthorities()))
        {
            throw new AccessDeniedException("");
        }
        thread.setValid(false);
        threadRepository.save(thread);
    }
}
