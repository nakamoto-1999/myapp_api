package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.logic.ColorLogic;
import com.example.admin.logic.ThreadLogic;
import com.example.admin.logic.AdminLogic;
import com.example.admin.logic.UserLogic;
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
    UserLogic userLogic;

    @Autowired
    ThreadLogic threadLogic;

    @Autowired
    ColorLogic colorLogic;

    @Override
    public void createPost(@NotNull HttpServletRequest req,@NotNull PostCreateRequest reqBody , Long threadId) {

        //スレッドへの書き込み---------------------------------------------------------------
        Thread thread = threadLogic.getEntityByThreadId(threadId);

        //IPに紐づいたユーザーが存在する場合はそのユーザーを取得し、存在しない場合は新しく作る
        User user = userLogic.isUserExistByIp(req.getRemoteAddr()) ?
                userLogic.getEntityByIp(req.getRemoteAddr()) :
                userLogic.createUser(req);

        if(!user.isPermitted()){
            throw new AccessDeniedException("アクセス規制中です。");
        }

        //Admin権限者、もしくはスレッドを立てたユーザー以外で、スレッドのブロックユーザーリストに登録されているユーザーは、アクセス拒否
        if(user.getUserId() != thread.getUser().getUserId() &&
                    threadUtil.isUserIdExistInBlockedList(thread.getBlockedUsers() , user.getUserId())) {
            throw new AccessDeniedException("スレッド主から追放されています。");
        }

        //スレッドがclosedの要件に該当するとき、書き込み不可とするストッパー
        if(threadUtil.isStopped(thread)) {
            throw new RuntimeException("スレッドが閉鎖されました。");
        }

        Post post = new Post();
        post.setUser(user);
        post.setThread(thread);
        post.setColor(colorLogic.getEntityByColorId(reqBody.getColorId()));
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
    public List<PostResponse> getAllResponsesByRequest(@NotNull HttpServletRequest req) {

        User user = userLogic.getEntityByIp(req.getRemoteAddr());
        List<Post> posts = user.getPosts();
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
