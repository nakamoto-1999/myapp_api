package com.example.admin.service;

import com.example.admin.compositekey.PkOfThreadAndUser;
import com.example.admin.entity.BlockedUserOfThread;
import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.logic.ColorLogic;
import com.example.admin.logic.ThreadLogic;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.BlockedUserOfThreadRepository;
import com.example.admin.repository.ThreadRepository;
import com.example.admin.request.ThreadConcludeRequest;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.ThreadUtil;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService{

    @Autowired
    ThreadRepository threadRepository;

    @Autowired
    BlockedUserOfThreadRepository blockedUserOfThreadRepository;

    @Autowired
    TimestampUtil timestampUtil;

    @Autowired
    UserUtil securityUtil;

    @Autowired
    ThreadUtil threadStopper;

    @Autowired
    ThreadLogic threadLogic;

    @Autowired
    ThreadUtil threadUtil;

    @Autowired
    UserLogic userLogic;

    @Autowired
    ColorLogic colorLogic;


    @Override
    public ThreadResponse createThread(@NotNull HttpServletRequest req, @NotNull ThreadCreateRequest reqBody) {

        Thread thread = new Thread();
        thread.setOverview(reqBody.getOverview());
        thread.setPoint(reqBody.getPoint());
        thread.setRed(reqBody.getRed());
        thread.setBlue(reqBody.getBlue());
        thread.setClosed(false);
        thread.setConcluded(false);
        User user = userLogic.getEntity(req);
        thread.setUser(user);
        thread.setDeleted(false);
        thread.setCreatedAt(timestampUtil.getNow());
        Thread createdThread = threadRepository.save(thread);

        return new ThreadResponse(createdThread);
    }

    @Override
    public List<ThreadResponse> getAllResponses() {
        List<Thread> threads = threadRepository.findAllByOrderByCreatedAtDesc();
        List<ThreadResponse> threadResponses = new ArrayList<>();
        threads.forEach(thread ->{
            threadResponses.add(new ThreadResponse(thread));
        });
        return threadResponses;
    }

    @Override
    public List<ThreadResponse> getAllResponseByUserId(Long userId ,@NotNull Authentication auth) {
        @NotNull MyUserDetails userDetails = auth.getPrincipal() instanceof MyUserDetails ?
                (MyUserDetails) auth.getPrincipal() : null;
        System.out.println(userId);
        if(userDetails.getUserId() != userId)
            throw new AccessDeniedException("");


        List<Thread> threads = threadRepository.findAllByUserIdOrderByThreadId(userId);
        List<ThreadResponse> threadResponses = new ArrayList<>();
        threads.forEach((Thread thread)->{
            threadResponses.add(new ThreadResponse(thread));
        });
        return threadResponses;
    }

    @Override
    public List<ThreadResponse> getAllResponseByKeyword(String keyword) {
        List<Thread> threads = threadRepository.findAllByKeyword(keyword);
        List<ThreadResponse> threadResponses = new ArrayList<>();
        threads.forEach(thread -> {
            threadResponses.add(new ThreadResponse(thread));
        });
        return threadResponses;
    }

    @Override
    public ThreadResponse getResponseByThreadId(Long threadId) {
        //データベース上からエンティティを取得
        Thread thread = threadLogic.getEntityByThreadId(threadId);
        //エンティティをレスポンス用オブジェクトに移し替え
        ThreadResponse threadResponse = new ThreadResponse(thread);
        threadResponse.setPosts(thread.getPosts());
        threadResponse.setBlockedUsers(thread.getBlockedUsers());
        return threadResponse;
    }

    @Override
    public void concludeByThreadId(Long threadId,@NotNull Authentication auth,
                                   @NotNull ThreadConcludeRequest reqBody) {

        @NotNull MyUserDetails userDetails = auth.getPrincipal() instanceof MyUserDetails?
                (MyUserDetails) auth.getPrincipal() : null;

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
        //reasonが空文字でなければ
        thread.setConclusionReason(reqBody.getConclusionReason());

        threadRepository.save(thread);

    }

    @Override
    public void blockUser(Long threadId, Long userId,@NotNull HttpServletRequest req) {

        //スレッドエンティティと、ブロック対象のユーザーエンティティを取得
        Thread thread = threadLogic.getEntityByThreadId(threadId);
        User user = userLogic.getEntityByUserId(userId);

        //リクエストを投げたユーザーとスレッドを立てたユーザーのIPアドレスが一致
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId() != thread.getUser().getUserId())
            throw new AccessDeniedException("");

        //管理者でログインしているユーザー、及び該当のスレッドを立てたユーザー、既にブロックリストへ追加ずみのユーザーはブロックリストに追加できない
        if(thread.getUser().getUserId() == user.getUserId() ||
                threadUtil.isUserIdExistInBlockedList(thread.getBlockedUsers() , user.getUserId())) {
            throw new RuntimeException("faild to add user into blocked list");
        }

        //ユーザーをブロックリストへ追加
        BlockedUserOfThread blockedUser = new BlockedUserOfThread(thread , user);
        blockedUserOfThreadRepository.save(blockedUser);

    }

    @Override
    public void unblockUser(Long threadId , Long userId ,@NotNull HttpServletRequest req){

        BlockedUserOfThread blockedUser
                = blockedUserOfThreadRepository.findById(new PkOfThreadAndUser(threadId , userId))
                .orElseThrow(() -> new IllegalArgumentException());

        //スレ主でなければ、アクセス拒否
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId() != threadId){
            throw new AccessDeniedException("");
        }
        blockedUserOfThreadRepository.delete(blockedUser);

    }

    @Override
    public void deleteByThreadId(Long threadId ,@NotNull HttpServletRequest req, @NotNull Authentication auth) {

        //IDからスレッドを取得
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //ユーザーが、認証を受けておらず、スレッドを立てたユーザーとも一致しない場合は、アクセス拒否
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId() != thread.getUser().getUserId() &&
                auth == null) {
            throw new AccessDeniedException("");
        }

        //認証を受けたユーザーがAdminではなく、isCloed、isConcludedのどちらかがtrue、またはisDeletedがtrueの場合は、例外を投げる
        if(auth != null
            && (thread.isClosed() || thread.isConcluded() ) || thread.isDeleted()) {
            throw new RuntimeException("faild to delete");
        }

        thread.setDeleted(true);
        threadRepository.save(thread);
    }
}
