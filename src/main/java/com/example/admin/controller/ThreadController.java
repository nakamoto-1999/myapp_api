package com.example.admin.controller;

import com.example.admin.request.PostCreateRequest;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.ThreadResponse;
import com.example.admin.service.PostService;
import com.example.admin.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ThreadController {

    @Autowired
    ThreadService threadService;

    @Autowired
    PostService postService;

    @PostMapping("/auth/thread/create")
    ResponseEntity<?> createThread(HttpServletRequest req, Authentication auth , @Validated @RequestBody ThreadCreateRequest reqBody){
        ThreadResponse response = threadService.createThread(auth , req , reqBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/thread/{threadId}/post/create")
    //IPアドレスを取得するためにHttpServletRequestを使用する
    ResponseEntity<?> createPost(Authentication auth ,HttpServletRequest req
            , @Validated @RequestBody PostCreateRequest reqBody ,@PathVariable Long threadId){

        postService.createPost(auth , req ,reqBody ,threadId);
        return ResponseEntity.ok(null);

    }

    @GetMapping("/thread")
    ResponseEntity<?> getAllThreads(){
        return ResponseEntity.ok(threadService.getAllResponses());
    }

    @GetMapping("/thread/{threadId}")
    ResponseEntity<?> getThreadByThreadId(@PathVariable Long threadId){

        ThreadResponse threadResponse = threadService.getResponseByThreadId(threadId);
        threadResponse.setPosts(postService.getAllResponseByThreadId(threadId));
        return ResponseEntity.ok(threadResponse);
    }


    @DeleteMapping("/auth/thread/{threadId}/delete")
    ResponseEntity<?> deleteThreadByThreadId(@PathVariable Long threadId , Authentication auth){
        threadService.deleteByThreadId(threadId , auth);
        return ResponseEntity.ok(null);
    }

}
