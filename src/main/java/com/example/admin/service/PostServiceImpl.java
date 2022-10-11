package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.logic.ColorLogic;
import com.example.admin.logic.ThreadLogic;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.PostRepository;
import com.example.admin.request.PostCreateRequest;
import com.example.admin.response.PostResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.ThreadStopperUtil;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import com.example.admin.utility.TimestampUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService{

    @Autowired
    PostRepository postRepository;

    @Autowired
    TimestampUtil timestampUtil;

    @Autowired
    UserUtil securityUtil;

    @Autowired
    ThreadStopperUtil threadStopper;

    @Autowired
    UserLogic userLogic;

    @Autowired
    ThreadLogic threadLogic;

    @Autowired
    ColorLogic colorLogic;

    @Override
    public void createPost(Authentication auth, HttpServletRequest req, PostCreateRequest reqBody , Long threadId) {

        if(auth == null || req == null || reqBody == null)throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        if(userDetails == null)throw new RuntimeException("required variables is null!");

        //許可されていないユーザーの場合は、アクセス拒否
        if(!userDetails.isPermitted())throw new AccessDeniedException("");

        //スレッドへの書き込み---------------------------------------------------------------
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //スレッドがclosedの要件に該当するとき、書き込み不可とするストッパー
        if(threadStopper.isStopped(thread)) {
            throw new RuntimeException("Thread Stopper worked!");
        }

        Post post = new Post();
        post.setUser(
                userLogic.getEntitiyByUserId(userDetails.getUserId())
        );
        post.setThread(thread);
        post.setIp(req.getRemoteAddr());
        post.setColor(
                colorLogic.getEntityByColorId(reqBody.getColorId())
        );
        post.setContent(reqBody.getContent());
        post.setDeleted(false);
        post.setCreatedAt(timestampUtil.getNow());
        postRepository.save(post);

    }

    @Override
    public List<PostResponse> getAllResponses() {

        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        //投稿情報をレスポンス用のリストに格納する
        posts.forEach((Post post)->{
            postResponses.add(this.getResponse(post));
        });

        return postResponses;
    }

    @Override
    public List<PostResponse> getAllResponsesByUserId(Long userId) {

        List<Post> posts = postRepository.findAllByUserIdOrderByPostId(userId);
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach((Post post)->{
            postResponses.add(new PostResponse(post));
        });

        return postResponses;
    }

    @Override
    public List<PostResponse> getAllResponseByThreadId(Long threadId) {
        List<Post> posts = postRepository.findAllByThreadIdOrderByPostId(threadId);
        List<PostResponse> postResponses = new ArrayList<>();
        posts.forEach((Post post)->{
            postResponses.add(new PostResponse(post));
        });
        return postResponses;
    }

    @Override
    public PostResponse getResponseByPostId(Long postId) {

        Post post = this.getEntityByPostId(postId);
        return new PostResponse(post);

    }

    public Post getEntityByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException());
        return post;
    }

    private PostResponse getResponse(Post post){
        return new PostResponse(post);
    }

    @Override
    public void deleteByPostId(Long postId , Authentication auth) {

        //IDからpostを取得
        Post post = this.getEntityByPostId(postId);

        if(auth == null)throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //認証を受けたユーザーが、Postを投稿したユーザーと一致せず、かつユーザーがADMINではない場合はアクセスを拒否
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , post.getUser().getUserId())
                && !securityUtil.isAdmin(userDetails.getAuthorities()))
                    throw new AccessDeniedException("");

        if(post.getThread().isClosed() || post.getThread().isConcluded() || post.isDeleted())
            throw new RuntimeException("faild to delete");
        post.setDeleted(true);
        postRepository.save(post);
    }

}
