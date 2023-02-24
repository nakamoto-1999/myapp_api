package com.example.admin.controller;

import com.example.admin.repository.PostRepository;
import com.example.admin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @GetMapping("/auth/post")
    ResponseEntity<?> getAll(){
        return ResponseEntity.ok(postService.getAllResponses());
    }

    @GetMapping("/auth/post/{postId}")
    ResponseEntity<?> getByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(postService.getResponseByPostId(postId));
    }

    @GetMapping("/user/post")
    ResponseEntity<?> getByRequest(HttpServletRequest req){
        return ResponseEntity.ok(postService.getAllResponsesByRequest(req));
    }

    @DeleteMapping("/auth/post/{postId}/delete")
    ResponseEntity<?> delete(Authentication auth , @PathVariable Long postId){
        postService.deleteByPostId(postId , auth);
        return ResponseEntity.ok(null);
    }

}
