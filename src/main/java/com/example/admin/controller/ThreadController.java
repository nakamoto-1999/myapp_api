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

    @PostMapping("/thread/create")
    ResponseEntity<?> createThread(HttpServletRequest req , @Validated @RequestBody ThreadCreateRequest reqBody){
        ThreadResponse response = threadService.createThread( req , reqBody);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/thread/{threadId}/post/create")
    //IPアドレスを取得するためにHttpServletRequestを使用する
    ResponseEntity<?> createPost( HttpServletRequest req
            , @Validated @RequestBody PostCreateRequest reqBody ,@PathVariable Long threadId){

        postService.createPost(req ,reqBody ,threadId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/thread")
    ResponseEntity<?> getAllThreads(){
        return ResponseEntity.ok(threadService.getAllResponses());
    }

    @GetMapping("/user/thread")
    ResponseEntity<?> getAllThreadByUserId(HttpServletRequest req){
        return ResponseEntity.ok(threadService.getAllResponseByRequest(req));
    }

    @GetMapping("/thread/search")
    ResponseEntity<?> getAllThreadsByKeyword(@RequestParam(value = "keyword") String keyword){
        return ResponseEntity.ok(threadService.getAllResponseByKeyword(keyword));
    }

    @GetMapping("/thread/{threadId}")
    ResponseEntity<?> getThreadByThreadId(@PathVariable Long threadId){
        ThreadResponse response = threadService.getResponseByThreadId(threadId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/thread/{threadId}/conclude")
    ResponseEntity<?> concludeByThreadId(@PathVariable Long threadId, HttpServletRequest req ,
                                         @Validated @RequestBody ThreadConcludeRequest reqBody) {
        threadService.concludeByThreadId(threadId , req , reqBody);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/thread/{threadId}/block/user/{userId}")
    ResponseEntity<?> blockUser(@PathVariable Long threadId , @PathVariable Long userId , HttpServletRequest req){
        threadService.blockUser(threadId , userId , req);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/thread/{threadId}/unblock/user/{userId}")
    ResponseEntity<?> unblockUser(@PathVariable Long threadId , @PathVariable Long userId , HttpServletRequest req){
        threadService.unblockUser(threadId , userId , req);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/thread/{threadId}/delete")
    ResponseEntity<?> deleteThreadByThreadId(@PathVariable Long threadId , HttpServletRequest req ) {
        threadService.deleteByThreadId(threadId , req);
        return ResponseEntity.ok(null);
    }

}
