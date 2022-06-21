package com.example.admin.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MyUserDtailsService extends UserDetailsService {

    UserDetails loadUserByUserId(Integer userId) throws UsernameNotFoundException;
}
