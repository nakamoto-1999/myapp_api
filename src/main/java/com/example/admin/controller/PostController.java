package com.example.admin.controller;

import com.example.admin.repository.PostRepository;
import com.example.admin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @GetMapping("/post/all")
    ResponseEntity<?> getAll(){
        return ResponseEntity.ok(postService.getAllResponses());
    }

    @GetMapping("/post/{postId}")
    ResponseEntity<?> getByPostId(@PathVariable Integer postId){
        return ResponseEntity.ok(postService.getResponseByPostId(postId));
    }

    @PutMapping("/post/{postId}/validate")
    ResponseEntity<?> validate(@PathVariable Integer postId){
        postService.validate(postId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/post/{postId}/invalidate")
    ResponseEntity<?> invalidate(@PathVariable Integer postId){
        postService.invalidate(postId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/post/{postId}/delete")
    ResponseEntity<?> delete(@PathVariable Integer postId){
        postService.delete(postId);
        return ResponseEntity.ok(null);
    }

}
