package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.User;
import com.example.admin.response.PostResponse;

import java.util.List;

public interface PostService {

    void create(String ip ,User user, String content);
    List<PostResponse> getAllResponses();
    PostResponse getResponseByPostId(Integer postId);
    List<PostResponse> getAllResponsesByUserId(Integer userId);
    void validate(Integer postId);
    void invalidate(Integer postId);
    void delete(Integer postId);
    void deleteAllByUserId(Integer userId);

}
