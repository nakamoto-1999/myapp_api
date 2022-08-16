package com.example.admin.utility;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UserUtilImpl implements UserUtil {

    @Override
    public boolean isAuthIdEqualPathId(Long authId, Long userId) {
        return authId == userId;
    }

    @Override
    public boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        for(GrantedAuthority authority : authorities){
            return authority.getAuthority().equals("ROLE_ADMIN");
        }
        return false;
    }
}
