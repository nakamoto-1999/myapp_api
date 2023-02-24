package com.example.admin.service;

import com.example.admin.entity.Admin;
import com.example.admin.entity.Role;
import com.example.admin.logic.AdminLogic;
import com.example.admin.repository.RoleRepository;
import com.example.admin.repository.AdminRepository;
import com.example.admin.request.UserCreateUpdateRequest;
import com.example.admin.response.AdminResponse;
import com.example.admin.security.MyUserDetails;
import com.example.admin.utility.TimestampUtil;
import com.example.admin.utility.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AdminLogic adminLogic;

    @Autowired
    TimestampUtil timestampUtil;

    @Autowired
    PasswordEncoder passwordEncorder;

    @Autowired
    UserUtil securityUtil;

    @Override
    public AdminResponse createAdmin(@NotNull UserCreateUpdateRequest reqBody) {

        Admin admin = new Admin();
        admin.setName(reqBody.getName());
        admin.setEmail(reqBody.getEmail());
        admin.setPassword(this.passwordEncorder.encode(reqBody.getPassword()));
        admin.setCreatedAt(timestampUtil.getNow());
        return new AdminResponse(adminRepository.save(admin));

    }

    @Override
    public List<AdminResponse> getAllResponses() {

        List<Admin> admins = adminRepository.findAllByOrderByAdminId();
        List<AdminResponse> userResponses = new ArrayList<>();
        admins.forEach((Admin admin)->{
            userResponses.add(new AdminResponse(admin));
        });

        return userResponses;
    }

    @Override
    //一般ユーザー用のデータレスポンス
    public AdminResponse getResponseByAdminId(Long adminId) {
        Admin admin = this.adminLogic.getEntityByAdminId(adminId);
        return new AdminResponse(admin);
    }

    @Override
    public AdminResponse getResponseByAuth(@NotNull Authentication auth) {
        @NotNull MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        Admin admin = adminLogic.getEntityByAdminId(userDetails.getUserId());
        return new AdminResponse(admin);
    }

    @Override
    public void updateByAdminId(Long adminId ,@NotNull Authentication auth ,@NotNull UserCreateUpdateRequest request ) {
        @NotNull MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //認証を受けたユーザーと削除予定のユーザーが一致しない場合は、アクセス拒否
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , adminId)){
            throw new AccessDeniedException("");
        }

        Admin admin = this.adminLogic.getEntityByAdminId(adminId);
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(
                this.passwordEncorder.encode(request.getPassword())
        );
        admin.setUpdatedAt(timestampUtil.getNow());
        adminRepository.save(admin);
    }

    //ユーザーの削除は、認証を受けたユーザーと削除対象のユーザーが同じユーザーか、AdminのRoleを持っているユーザーのみ許可する
    @Override
    public void deleteByAdminId(Long adminId ,@NotNull Authentication auth) {

        @NotNull MyUserDetails userDetails =
                auth.getPrincipal() instanceof MyUserDetails ? (MyUserDetails) auth.getPrincipal() :null;

        //管理者の取得
        Admin admin = this.adminLogic.getEntityByAdminId(adminId);

        //認証を受けたユーザーが削除対象のユーザーと一致しない場合はアクセス拒否
        if(userDetails == null)throw new RuntimeException("required variables is null!");
        if(!securityUtil.isAuthIdEqualPathId(userDetails.getUserId() , adminId )){
            throw new AccessDeniedException("");
        }
        adminRepository.delete(admin);

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
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        return admin != null;
    }
}
