package com.example.admin.service;

import com.example.admin.entity.Post;
import com.example.admin.entity.User;
import com.example.admin.request.PostCreateRequest;
import com.example.admin.response.PostResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    void createPost( HttpServletRequest req , PostCreateRequest reqBody , Long threadId);
    List<PostResponse> getAllResponses();
    PostResponse getResponseByPostId(Long postId);
    List<PostResponse> getAllResponsesByRequest(HttpServletRequest req);
    List<PostResponse> getAllResponseByThreadId(Long threadId);
    void deleteByPostId(Long postId , Authentication auth);

}
