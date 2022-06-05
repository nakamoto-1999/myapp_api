package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.User;
import com.example.admin.repository.PostRepository;
import com.example.admin.response.PostResponse;
import com.example.admin.response.UserResponse;
import com.example.admin.utility.TimestampManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService{

    @Autowired
    PostRepository postRepository;

    @Autowired
    TimestampManager timestampManager;

    @Override
    public void create(String ip ,User user, String content) {
        Post post = new Post();
        post.setIp(ip);
        post.setUser(user);
        post.setContent(content);
        post.setCreatedAt(timestampManager.getNow());
        postRepository.save(post);
    }

    @Override
    public List<PostResponse> getAllResponses() {
        List<Post> posts = postRepository.findAll();
        List<PostResponse> postResponses = new ArrayList<>();
        //投稿情報をレスポンス用のリストに格納する
        for(Post post :posts){
            postResponses.add(this.getResponse(post));
        }
        return postResponses;
    }

    @Override
    public List<PostResponse> getAllResponsesByUserId(Integer userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        List<PostResponse> postResponses = new ArrayList<>();
        for(Post post : posts){
            postResponses.add(this.getResponse(post));
        }
        return postResponses;
    }

    @Override
    public PostResponse getResponseByPostId(Integer postId) {
        Post post = this.getEntityByPostId(postId);
        return this.getResponse(post);
    }

    @Override
    public Post getEntityByPostId(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new InvalidConfigurationPropertyValueException("Post Not Found" , postId , "Post Not Found By PostID"));
        return post;
    }

    private PostResponse getResponse(Post post){
        return new PostResponse(
                post.getPostId(),
                post.getIp(),
                this.getUserResponse(post.getUser()),
                post.getContent(),
                post.getIsValid(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    private UserResponse getUserResponse(User user){
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getIsValid(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public void validate(Integer postId) {
        Post post = this.getEntityByPostId(postId);
        post.setIsValid(true);
        post.setUpdatedAt(timestampManager.getNow());
        postRepository.save(post);
    }

    @Override
    public void invalidate(Integer postId) {
        Post post = this.getEntityByPostId(postId);
        post.setIsValid(false);
        post.setUpdatedAt(timestampManager.getNow());
        postRepository.save(post);
    }

    @Override
    public void delete(Integer postId) {
        Post post = this.getEntityByPostId(postId);
        postRepository.delete(post);
    }

    @Override
    public void deleteAllByUserId(Integer userId) {
        postRepository.deleteAllByUserId(userId);
    }

}
