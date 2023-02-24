package com.example.admin.security;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MyUserDetailsImpl implements MyUserDetails {

    private final Admin admin;

    public MyUserDetailsImpl(Admin admin){
        this.admin = admin;
    }

    @Override
    public Long getUserId(){
        return this.admin.getAdminId();
    }

    @Override
    public String getEmail() {
        return this.admin.getEmail();
    }

    @Override
    public boolean isPermitted(){
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_ADMIN");
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
