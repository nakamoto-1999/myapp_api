package com.example.admin.security;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(email == null || "".equals(email)){
            throw new UsernameNotFoundException(email + " Not Found!");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException(email + " Not Found!");
        }

        return new MyUserDetailsImpl(user);
    }

    @Override
    public UserDetails loadUserByUserId(Integer userId) throws UsernameNotFoundException{

        if(userId == null){
            throw new UsernameNotFoundException(userId + "Not Found");
        }

        User user = this.userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new UsernameNotFoundException(userId + "Not Found");
        }

        return new MyUserDetailsImpl(user);
    }

}
