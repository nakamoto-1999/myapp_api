package com.example.admin.controller;

import com.example.admin.repository.PostRepository;
import com.example.admin.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("http://localhost:3000")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @GetMapping("/post/all")
    ResponseEntity<?> getAll(){
        return ResponseEntity.ok(postService.getAllResponses());
    }

    @GetMapping("admin/post/{postId}")
    ResponseEntity<?> getByPostId(@PathVariable Integer postId){
        return ResponseEntity.ok(postService.getResponseByPostId(postId));
    }

    @PutMapping("/admin/post/{postId}/validate")
    ResponseEntity<?> validate(@PathVariable Integer postId){
        postService.validate(postId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/admin/post/{postId}/invalidate")
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
