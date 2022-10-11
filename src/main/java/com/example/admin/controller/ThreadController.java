package com.example.admin.controller;

import com.example.admin.request.PostCreateRequest;
import com.example.admin.request.ThreadConcludeRequest;
import com.example.admin.request.ThreadCreateRequest;
import com.example.admin.response.PostResponse;
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
        ThreadResponse response = threadService.getResponseByThreadId(threadId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/auth/thread/{threadId}/conclude")
    ResponseEntity<?> concludeByThreadId(@PathVariable Long threadId, Authentication auth ,
                                         @Validated @RequestBody ThreadConcludeRequest reqBody) {
        threadService.concludeByThreadId(threadId , auth , reqBody);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/auth/thread/{threadId}/user/{userId}/block")
    ResponseEntity<?> blockUser(@PathVariable Long threadId , @PathVariable Long userId , Authentication auth){
        threadService.blockUser(threadId , userId , auth);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/auth/thread/{threadId}/user/{userId}/unblock")
    ResponseEntity<?> unblockUser(@PathVariable Long threadId , @PathVariable Long userId , Authentication auth){
        threadService.unblockUser(threadId , userId , auth);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/auth/thread/{threadId}/delete")
    ResponseEntity<?> deleteThreadByThreadId(@PathVariable Long threadId , Authentication auth){
        threadService.deleteByThreadId(threadId , auth);
        return ResponseEntity.ok(null);
    }

}
