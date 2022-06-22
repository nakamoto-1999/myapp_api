package com.example.admin.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtilForControllerImpl implements SecurityUtilForController{

    @Override
    public boolean isAuthIdEqualPathId(Integer authId, Integer pathId) {
        return authId == pathId;
    }

    @Override
    public boolean isAdmin(Collection<? extends GrantedAuthority> authorities) {
        for(GrantedAuthority authority : authorities){
            return authority.getAuthority().equals("ROLE_ADMIN");
        }
        return false;
    }
}
