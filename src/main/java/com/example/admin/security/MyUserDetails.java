package com.example.admin.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface MyUserDetails extends UserDetails {

    Long getUserId();
    String getEmail();
    boolean isPermitted();

}
