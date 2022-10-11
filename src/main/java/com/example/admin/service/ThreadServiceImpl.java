package com.example.admin.service;

import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.logic.ColorLogic;
import com.example.admin.logic.ThreadLogic;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.ThreadRepository;
import com.example.admin.request.ThreadConcludeRequest;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.ThreadStopperUtil;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
    ThreadStopperUtil threadStopper;

    @Autowired
    ThreadLogic threadLogic;

    @Autowired
    UserLogic userLogic;

    @Autowired
    ColorLogic colorLogic;


    @Override
    public ThreadResponse createThread(Authentication auth, HttpServletRequest req, ThreadCreateRequest reqBody) {

        if(auth == null ||req == null || reqBody == null ) throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        if(userDetails == null) throw new RuntimeException("required variables is null!");

        //許可されていないユーザーの場合は、アクセス拒否
        if(!userDetails.isPermitted()) throw new AccessDeniedException("");

        Thread thread = new Thread();
        thread.setOverview(reqBody.getOverview());
        thread.setPoint(reqBody.getPoint());
        thread.setRed(reqBody.getRed());
        thread.setBlue(reqBody.getBlue());
        thread.setClosed(false);
        thread.setConcluded(false);
        User user = userLogic.getEntitiyByUserId(userDetails.getUserId());
        thread.setUser(user);
        thread.setIp(req.getRemoteAddr());
        thread.setDeleted(false);
        thread.setCreatedAt(timestampUtil.getNow());
        Thread createdThread = threadRepository.save(thread);

        return new ThreadResponse(createdThread);
    }

    @Override
    public List<ThreadResponse> getAllResponses() {
        List<Thread> threads = threadRepository.findAllByOrderByThreadId();
        List<ThreadResponse> threadResponses = new ArrayList<>();
        threads.forEach(thread ->{
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
    public void concludeByThreadId(Long threadId, Authentication auth, ThreadConcludeRequest reqBody) {

        if(auth == null || reqBody == null)
            throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails = auth.getPrincipal() instanceof MyUserDetails?
                (MyUserDetails) auth.getPrincipal() : null;

        if(userDetails == null)
            throw new RuntimeException("required variables is null!");

        Thread thread = threadLogic.getEntityByThreadId(threadId);
        //認可を受けたユーザーとスレッドを立てたユーザーが異なるとき、アクセス拒否
        if( userDetails.getUserId() != thread.getUser().getUserId() )
            throw new AccessDeniedException("");

        //isConcludedがtrueの場合、これ以下を実行しない
        if(thread.isConcluded())return;
        thread.setClosed(true);
        thread.setConcluded(true);
        thread.setConcludedColor(
                colorLogic.getEntityByColorId(reqBody.getConcludedColorId())
        );
        thread.setConclusionReason(reqBody.getConclusionReason());
        threadRepository.save(thread);

    }

    @Override
    public void deleteByThreadId(Long threadId , Authentication auth) {

        //IDからスレッドを取得
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //スレッドを立てたユーザーと、認証を受けたユーザーが一致しなければアクセス拒否
        if(auth == null) throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        if(userDetails == null)throw new RuntimeException("required variables is null!");

        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , thread.getUser().getUserId())
                && !securityUtil.isAdmin(userDetails.getAuthorities()))
                    throw new AccessDeniedException("");

        //isConcludedもしくはisDeletedがtrueの場合は、以下を実行しない
        if(thread.isClosed() || thread.isConcluded() || thread.isDeleted())
            throw new RuntimeException("faild to delete");

        thread.setDeleted(true);
        threadRepository.save(thread);
    }
}
