package com.example.admin.controller;

import com.example.admin.repository.UserRepository;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.service.PostService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @PostMapping("/user/create")
    public ResponseEntity<?> create(@Validated @RequestBody UserCreateUpdateRequest request){
        //一般ユーザーの場合はroleIdが1
        userService.create(request.getName() , request.getEmail() , request.getPassword() , 1);
        return ResponseEntity.ok(null);
    }

    @PostMapping("auth/admin/user/create")
    public ResponseEntity<?> createAdmin(@Validated @RequestBody UserCreateUpdateRequest request){
        //一般ユーザーの場合はroleIdが1
        userService.create(request.getName() , request.getEmail() , request.getPassword() , 2);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/admin/user/all")
    //一般ユーザーの場合はroleIdが2
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Integer userId){
        UserResponse response = userService.getResponseByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/user/{userId}/posts")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Integer userId){
        //ユーザーの投稿も一括で取得する
        return ResponseEntity.ok(
                postService.getAllResponsesByUserId(userId)
        );
    }

    @PutMapping("user/{userId}/update")
    public ResponseEntity<?> update(@PathVariable Integer userId , @RequestBody UserCreateUpdateRequest request){
        userService.update(userId , request.getName() , request.getEmail() , request.getPassword());
        return ResponseEntity.ok(null);
    }

    @PutMapping("admin/user/{userId}/validate")
    public ResponseEntity<?> validate(@PathVariable Integer userId){
        userService.validate(userId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("admin/user/{userId}/invalidate")
    public ResponseEntity<?> invalidate(@PathVariable Integer userId){
        userService.invalidate(userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("admin/user/{userId}/delete")
    public ResponseEntity<?> delete(@PathVariable Integer userId){
        userService.delete(userId);
        postService.deleteAllByUserId(userId);
        return ResponseEntity.ok(null);
    }


}
