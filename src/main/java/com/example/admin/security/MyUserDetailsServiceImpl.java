package com.example.admin.security;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import com.example.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(email == null || "".equals(email)){
            throw new UsernameNotFoundException(email + " Not Found!");
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if(admin == null){
            throw new UsernameNotFoundException(email + " Not Found!");
        }

        return new MyUserDetailsImpl(admin);
    }

    @Override
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException{

        if(userId == null){
            throw new UsernameNotFoundException(userId + "Not Found");
        }

        Admin admin = this.adminRepository.findById(userId).orElse(null);
        if(admin == null){
            throw new UsernameNotFoundException(userId + "Not Found");
        }

        return new MyUserDetailsImpl(admin);
    }

}
