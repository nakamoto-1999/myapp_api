package com.example.admin.service;

import com.example.admin.request.ThreadConcludeRequest;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThreadService {

    ThreadResponse createThread(HttpServletRequest req, ThreadCreateRequest reqBody);
    List<ThreadResponse> getAllResponses();
    List<ThreadResponse> getAllResponseByRequest(HttpServletRequest req);
    List<ThreadResponse> getAllResponseByKeyword(String keyword);
    ThreadResponse getResponseByThreadId(Long threadId);
    void concludeByThreadId(Long threadId , HttpServletRequest req , ThreadConcludeRequest reqBody);
    void deleteByThreadId(Long threadId , HttpServletRequest req );
    void blockUser(Long threadId ,Long userId , HttpServletRequest req);
    void unblockUser(Long threadId , Long userId , HttpServletRequest req );

}
