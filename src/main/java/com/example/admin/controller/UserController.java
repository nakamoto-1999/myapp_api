package com.example.admin.controller;

import com.example.admin.repository.UserRepository;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.PostResponse;
import com.example.admin.response.UserResponse;
import com.example.admin.service.PostService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;


    @GetMapping("/user/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Integer userId){
        UserResponse response = userService.getResponseByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Integer userId){
        //ユーザーの投稿も一括で取得する
        return ResponseEntity.ok(
                postService.getAllResponsesByUserId(userId)
        );
    }

    @PutMapping("/user/{userId}/validate")
    public ResponseEntity<?> validate(@PathVariable Integer userId){
        userService.validate(userId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/user/{userId}/invalidate")
    public ResponseEntity<?> invalidate(@PathVariable Integer userId){
        userService.invalidate(userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer userId){
        userService.delete(userId);
        postService.deleteAllByUserId(userId);
        return ResponseEntity.ok(null);
    }

}
