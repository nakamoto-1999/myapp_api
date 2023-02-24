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

        //IPに紐づいたユーザーがすでに存在する場合は該当ユーザーを、存在しない場合は新しく作る
        User user = userLogic.isUserExistByIp(req.getRemoteAddr()) ?
                userLogic.getEntityByIp(req.getRemoteAddr())
                :
                userLogic.createUser(req);

        if(!user.isPermitted()){
            throw new AccessDeniedException("アクセス規制中です。");
        }

        Thread thread = new Thread();
        thread.setOverview(reqBody.getOverview());
        thread.setPoint(reqBody.getPoint());
        thread.setRed(reqBody.getRed());
        thread.setBlue(reqBody.getBlue());
        thread.setClosed(false);
        thread.setConcluded(false);
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
    public List<ThreadResponse> getAllResponseByRequest(@NotNull HttpServletRequest req) {

        User user = userLogic.getEntityByIp(req.getRemoteAddr());
        //List<Thread> threads = threadRepository.findAllByUserIdOrderByThreadId(user.getUserId());
        List<Thread> threads = user.getThreads();
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
    public void concludeByThreadId(Long threadId,@NotNull HttpServletRequest req,
                                   @NotNull ThreadConcludeRequest reqBody) {


        Thread thread = threadLogic.getEntityByThreadId(threadId);
        //認可を受けたユーザーとスレッドを立てたユーザーが異なるとき、アクセス拒否
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId() != thread.getUser().getUserId() ){
            throw new AccessDeniedException("");
        }

        //すでに判決が下されている、またはスレッドが削除済みの場合はエラー
        if(thread.isConcluded() || thread.isDeleted()){
            throw new RuntimeException("faild to conclude thread");
        }

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

        //リクエストを投げたユーザーとスレッドを立てたユーザーのIPアドレスが一致しているか
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId()
                != thread.getUser().getUserId()) {
            throw new AccessDeniedException("");
        }

        //スレッドを立てたユーザー、ブロックリストへ追加済みのユーザーはブロックリストに追加できない
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

        //リクエストを投げたユーザーとスレッドを立てたユーザーのIPアドレスが一致しているか
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId()
                != blockedUser.getThread().getUser().getUserId()){
            throw new AccessDeniedException("");
        }
        blockedUserOfThreadRepository.delete(blockedUser);

    }

    @Override
    public void deleteByThreadId(Long threadId ,@NotNull HttpServletRequest req) {

        //IDからスレッドを取得
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //リクエストを投げたユーザーとスレッドを立てたユーザーのIPアドレスが一致しているか
        if(userLogic.getEntityByIp(req.getRemoteAddr()).getUserId() != thread.getUser().getUserId() ) {
            throw new AccessDeniedException("");
        }

        //isCloed、isConcluded、isDeletedのいずれかがtrueの場合は、例外を投げる
        if(thread.isClosed() || thread.isConcluded() || thread.isDeleted()) {
            throw new RuntimeException("faild to delete");
        }

        thread.setDeleted(true);
        threadRepository.save(thread);
    }
}
