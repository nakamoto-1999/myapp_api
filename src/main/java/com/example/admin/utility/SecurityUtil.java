package com.example.admin.utility;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface SecurityUtil {
    boolean isAuthIdEqualPathId(Long authId , Long pathId);
    boolean isAdmin(Collection<? extends GrantedAuthority> authorities);
}
