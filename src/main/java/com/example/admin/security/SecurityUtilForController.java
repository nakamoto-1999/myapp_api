package com.example.admin.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface SecurityUtilForController {
    boolean isAuthIdEqualPathId(Integer authId , Integer pathId);
    boolean isAdmin(Collection<? extends GrantedAuthority> authorities);
}
