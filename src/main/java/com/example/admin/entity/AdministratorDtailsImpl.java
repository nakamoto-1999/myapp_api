package com.example.admin.entity;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class AdministratorDtailsImpl implements UserDetails {

    Collection<? extends GrantedAuthority> authorities;
    Administrator administrator;

    public AdministratorDtailsImpl(Administrator administrator , Collection<? extends GrantedAuthority> authorities ){
        this.administrator = administrator;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.administrator.getPassword();
    }

    @Override
    public String getUsername() {
        return this.administrator.getName();
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
