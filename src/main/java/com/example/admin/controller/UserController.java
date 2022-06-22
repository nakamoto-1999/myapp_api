package com.example.admin.controller;

import com.example.admin.repository.UserRepository;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.security.SecurityUtilForController;
import com.example.admin.service.PostService;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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

    @Autowired
    SecurityUtilForController securityUtil;

    @PostMapping("/public/user/create")
    public ResponseEntity<?> create(@Validated @RequestBody UserCreateUpdateRequest request){
        //一般ユーザーの場合はroleIdが1
        userService.create(request.getName() , request.getEmail() , request.getPassword() , 1);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/auth/admin/user/create")
    public ResponseEntity<?> createAdmin(@Validated @RequestBody UserCreateUpdateRequest request){
        //一般ユーザーの場合はroleIdが1
        userService.create(request.getName() , request.getEmail() , request.getPassword() , 2);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/auth/admin/user/all")
    //一般ユーザーの場合はroleIdが2
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(userService.getAllResponses());
    }

    @GetMapping("/public/user/{userId}")
    public ResponseEntity<?> getByUserId(@PathVariable Integer userId){
        UserResponse response = userService.getResponseByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/user/{userId}/posts")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Integer userId){
        //ユーザーの投稿も一括で取得する
        return ResponseEntity.ok(
                postService.getAllResponsesByUserId(userId)
        );
    }

    @PutMapping("/auth/user/{userId}/update")
    public ResponseEntity<?> updateByUserId(@PathVariable Integer userId ,@Validated @RequestBody UserCreateUpdateRequest request, Authentication auth){
        MyUserDetails userDetails = null;
        if(auth != null){
            userDetails = (MyUserDetails) auth.getPrincipal();
        };
        //リクエストされたUserIdが認可を受けたユーザーと一致しないアクセス拒否
        if(userDetails != null){
            if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , userId)){
                throw new AccessDeniedException("");
            }
        }
        userService.update(userId , request.getName() , request.getEmail() ,request.getPassword());
        return ResponseEntity.ok(null);
    }

    @PutMapping("/auth/admin/user/{userId}/validate")
    public ResponseEntity<?> validate(@PathVariable Integer userId){
        userService.validate(userId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/auth/admin/user/{userId}/invalidate")
    public ResponseEntity<?> invalidate(@PathVariable Integer userId){
        userService.invalidate(userId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/auth/user/{userId}/delete")
    public ResponseEntity<?> deleteByUserId(@PathVariable Integer userId , Authentication auth){
        MyUserDetails userDetails = null;
        if(auth != null){
            userDetails = (MyUserDetails) auth.getPrincipal();
        };
        //リクエストされたUserIdが一致せず、かつADMIN権限を持っていない場合はアクセス拒否
        if(userDetails != null){
            if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , userId) && !securityUtil.isAdmin(userDetails.getAuthorities())){
                throw new AccessDeniedException("");
            }
        }
        userService.delete(userId);
        return ResponseEntity.ok(null);
    }

}

