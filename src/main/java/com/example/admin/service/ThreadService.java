package com.example.admin.service;

import com.example.admin.request.ThreadConcludeRequest;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThreadService {

    ThreadResponse createThread(Authentication auth , HttpServletRequest req, ThreadCreateRequest reqBody);
    List<ThreadResponse> getAllResponses();
    List<ThreadResponse> getAllResponseByUserId(Long userId , Authentication auth);
    List<ThreadResponse> getAllResponseByKeyword(String keyword);
    ThreadResponse getResponseByThreadId(Long threadId);
    void concludeByThreadId(Long threadId , Authentication auth , ThreadConcludeRequest reqBody);
    void deleteByThreadId(Long threadId , Authentication auth );
    void blockUser(Long threadId ,Long userId , Authentication auth);
    void unblockUser(Long threadId , Long userId , Authentication auth);

}
