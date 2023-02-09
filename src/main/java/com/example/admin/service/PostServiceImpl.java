package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.Thread;
import com.example.admin.logic.ColorLogic;
import com.example.admin.logic.ThreadLogic;
import com.example.admin.logic.AdminLogic;
import com.example.admin.repository.PostRepository;
import com.example.admin.request.PostCreateRequest;
import com.example.admin.response.PostResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.ThreadUtil;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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
    ThreadUtil threadUtil;

    @Autowired
    AdminLogic userLogic;

    @Autowired
    ThreadLogic threadLogic;

    @Autowired
    ColorLogic colorLogic;

    @Override
    public void createPost(@NotNull Authentication auth,@NotNull HttpServletRequest req,@NotNull PostCreateRequest reqBody ,
                           Long threadId) {

        @NotNull MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //許可されていないユーザーの場合アクセス拒否
        if(!userDetails.isPermitted())
            throw new AccessDeniedException("");

        //スレッドへの書き込み---------------------------------------------------------------
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //Admin権限者、もしくはスレッドを立てたユーザー以外で、スレッドのブロックユーザーリストに登録されているユーザーは、アクセス拒否
        if(!securityUtil.isAdmin(userDetails.getAuthorities()) &&
                userDetails.getUserId() != thread.getUser().getUserId() &&
                    threadUtil.isUserIdExistInBlockedList(thread.getBlockedUsers() , userDetails.getUserId()))
                        throw new AccessDeniedException("blocked by thread owner!");

        //スレッドがclosedの要件に該当するとき、書き込み不可とするストッパー
        if(threadUtil.isStopped(thread))
            throw new RuntimeException("Thread Stopper worked!");

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
    public void deleteByPostId(Long postId ,@NotNull Authentication auth) {

        @NotNull MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //IDからpostを取得
        Post post = this.getEntityByPostId(postId);

        /*認証を受けたユーザーのRoleが、Adminでない場合はアクセス拒否
        */
        if(!securityUtil.isAdmin(userDetails.getAuthorities()))
            throw new AccessDeniedException("");

        //認証を受けたユーザーがAdminではなく、isCloed、isConcludedのどちらかがtrue、またはisDeletedがtrueの場合は、例外を投げる
        if(post.isDeleted())
            throw new RuntimeException("faild to delete");

        post.setDeleted(true);
        postRepository.save(post);
    }

}
