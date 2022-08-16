package com.example.admin.service;

import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ThreadService {

    ThreadResponse createThread(Authentication auth , HttpServletRequest req, ThreadCreateRequest reqBody);
    List<ThreadResponse> getAllResponses();
    List<ThreadResponse> getAllResponseByUserId(Long userId);
    ThreadResponse getResponseByThreadId(Long threadId);
    void validateByThreadId(Long threadId);
    void invalidateByThreadId(Long threadId);
    void deleteByThreadId(Long threadId , Authentication auth );

}
