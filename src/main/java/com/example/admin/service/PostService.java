package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.User;
import com.example.admin.request.PostCreateRequest;
import com.example.admin.response.PostResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    void createPost(Authentication auth , HttpServletRequest req , PostCreateRequest reqBody , Long threadId);
    List<PostResponse> getAllResponses();
    PostResponse getResponseByPostId(Long postId);
    List<PostResponse> getAllResponsesByUserId(Long userId);
    List<PostResponse> getAllResponseByThreadId(Long threadId);
    void validateByPostId(Long postId);
    void invalidateByPostId(Long postId);
    void deleteByPostId(Long postId , Authentication auth);

}
