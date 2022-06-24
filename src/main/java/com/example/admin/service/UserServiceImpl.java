package com.example.admin.service;

import com.example.admin.entity.User;
import com.example.admin.logic.UserLogic;
import com.example.admin.repository.RoleRepository;
import com.example.admin.repository.UserRepository;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.UserResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.SecurityUtil;
import com.example.admin.utility.TimestampUtil;
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
    TimestampUtil timestampManager;

    @Autowired
    PasswordEncoder passwordEncorder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserLogic userLogic;

    @Autowired
    SecurityUtil securityUtil;

    private void create(String name, String email, String password , Long roleId) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(this.passwordEncorder.encode(password));
        user.setValid(true);
        user.setRole(roleRepository.getById(roleId));
        //user.setRoleId(roleId);
        user.setCreatedAt(timestampManager.getNow());
        System.out.println(user);
        userRepository.save(user);
    }

    @Override
    public void createGeneralUser(UserCreateUpdateRequest request) {
        if(request != null) {return;}
        create(request.getName(), request.getEmail() , request.getPassword() , 1L);

    }

    @Override
    public void createAdminUser(UserCreateUpdateRequest request) {
        if(request != null) {return;}
        create(request.getName(), request.getEmail() , request.getPassword() , 2L);
    }

    @Override
    public List<UserResponse> getAllResponses() {

        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        users.forEach((User user)->{
            userResponses.add(new UserResponse(user));
        });

        return userResponses;
    }

    @Override
    public UserResponse getResponseByUserId(Long userId) {
        User user = this.userLogic.getEntitiyByUserId(userId);
        return new UserResponse(user);
    }

    @Override
    public void updateByUserId(Long userId , Authentication auth , UserCreateUpdateRequest request ) {
        if(auth == null || request == null){return;}
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        //認証を受けたユーザーと取得したユーザーが一致しない場合は、アクセス拒否
        if(userDetails == null){return;}
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , userId)){
            throw new AccessDeniedException("");
        }

        User user = this.userLogic.getEntitiyByUserId(userId);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                this.passwordEncorder.encode(request.getPassword())
        );
        user.setUpdatedAt(timestampManager.getNow());
        userRepository.save(user);
    }

    @Override
    public void validateByUserId(Long userId) {
        User user = this.userLogic.getEntitiyByUserId(userId);
        if(!user.isValid()) {
            user.setValid(true);
            user.setUpdatedAt(timestampManager.getNow());
            userRepository.save(user);
        }
    }

    @Override
    public void invalidateByUserId(Long userId) {
        User user = this.userLogic.getEntitiyByUserId(userId);
        if(user.isValid()) {
            user.setValid(false);
            user.setUpdatedAt(timestampManager.getNow());
            userRepository.save(user);
        }
    }

    //ユーザーの削除は、認証を受けたユーザーと削除対象のユーザーが同じユーザーか、AdminのRoleを持っているユーザーのみ許可する
    @Override
    public void deleteByUserId(Long userId , Authentication auth) {

        if(auth == null)return;
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();

        //認証を受けたユーザーを取得したユーザーが一致せず、RoleもAdminではない場合はアクセス拒否
        if(userDetails == null){return;}
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , userId) && !securityUtil.isAdmin(userDetails.getAuthorities())){
            throw new AccessDeniedException("");
        }

        User user = this.userLogic.getEntitiyByUserId(userId);
        userRepository.delete(user);
    }

}
