package com.example.admin.service;

import com.example.admin.entity.Role;
import com.example.admin.entity.User;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.RoleRepository;
import com.example.admin.repository.UserRepository;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.response.UserResponseAuth;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import com.example.admin.utility.TimestampUtilImpl;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    TimestampUtil timestampUtil;

    @Autowired
    PasswordEncoder passwordEncorder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserLogic userLogic;

    @Autowired
    UserUtil securityUtil;

    private User create(String name, String email, String password , Long roleId) {
        System.out.println(name);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(this.passwordEncorder.encode(password));
        user.setPermitted(true);
        user.setDeleted(false);
        Role role = roleRepository.getById(roleId);
        user.setRole(role);
        user.setCreatedAt(timestampUtil.getNow());
        return userRepository.save(user);
    }

    @Override
    public UserResponse createGeneralUser(UserCreateUpdateRequest request) {
        System.out.println(request);
        if(request == null) {return new UserResponse();}
        User createdUser = create(request.getName(), request.getEmail() , request.getPassword() , 1L);
        if(createdUser == null) {return new UserResponse();}
        return new UserResponse(createdUser);
    }

    @Override
    public UserResponse createAdminUser(UserCreateUpdateRequest request) {
        if(request == null) {return new UserResponse();}
        User createdUser = create(request.getName(), request.getEmail() , request.getPassword() , 2L);
        if(createdUser == null) {return new UserResponse();}
        return new UserResponse(createdUser);
    }

    @Override
    public List<UserResponse> getAllResponses() {

        List<User> users = userRepository.findAllByOrderByUserId();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach((User user)->{
            userResponses.add(new UserResponse(user));
        });

        return userResponses;
    }

    @Override
    //認証を受けたユーザー用のデータレスポンス
    public UserResponseAuth getResponseAuthByUserId(Long userId) {
        User user = this.userLogic.getEntitiyByUserId(userId);
        return new UserResponseAuth(user);
    }

    @Override
    //一般ユーザー用のデータレスポンス
    public UserResponse getResponseByUserId(Long userId) {
        User user = this.userLogic.getEntitiyByUserId(userId);
        return new UserResponse(user);
    }

    @Override
    public UserResponse getResponseByAuth(Authentication auth) {
        if(auth == null)throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        User user = userLogic.getEntitiyByUserId(userDetails.getUserId());
        return new UserResponse(user);
    }

    @Override
    public void updateByUserId(Long userId , Authentication auth , UserCreateUpdateRequest request ) {
        if(auth == null || request == null)throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //認証を受けたユーザーと取得したユーザーが一致しない場合は、アクセス拒否
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , userId)){
            throw new AccessDeniedException("");
        }

        User user = this.userLogic.getEntitiyByUserId(userId);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                this.passwordEncorder.encode(request.getPassword())
        );
        user.setUpdatedAt(timestampUtil.getNow());
        userRepository.save(user);
    }

    @Override
    public void switchPermitByUserId(Long userId) {
        User user = this.userLogic.getEntitiyByUserId(userId);
        user.setPermitted(!user.isPermitted());
        userRepository.save(user);
    }

    //ユーザーの削除は、認証を受けたユーザーと削除対象のユーザーが同じユーザーか、AdminのRoleを持っているユーザーのみ許可する
    @Override
    public void deleteByUserId(Long userId , Authentication auth) {

        if(auth == null)throw new RuntimeException("required variables is null!");
        MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //ユーザーの取得
        User user = this.userLogic.getEntitiyByUserId(userId);

        //認証を受けたユーザーを取得したユーザーが一致せず、RoleもAdminではない場合はアクセス拒否
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , user.getUserId()) && !securityUtil.isAdmin(userDetails.getAuthorities())){
            throw new AccessDeniedException("");
        }

        if(user.isDeleted())throw new RuntimeException("faild to delete");
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public boolean isExistEmail(Authentication auth , String email) {
        if(auth != null) {
            MyUserDetails userDetails =
                    auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;
            //認証を受けたユーザーが、全く同じメールアドレスを登録しようとしているときは、false
            if(userDetails != null && userDetails.getEmail().equals(email)){
                return false;
            }
        }
        //認証を受けていない、または認証を受けているが、既存のメールアドレスと異なるものを登録しようとしているとき
        //登録しようとしているメールアドレスが、すでに存在するならば、true
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }
}
