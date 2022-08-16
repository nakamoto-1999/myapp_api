package com.example.admin.utility;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface UserUtil {
    boolean isAuthIdEqualPathId(Long authId , Long userId);
    boolean isAdmin(Collection<? extends GrantedAuthority> authorities);
}
