package com.example.admin.service;

import com.example.admin.entity.Administrator;
import com.example.admin.entity.AdministratorDtailsImpl;
import com.example.admin.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdministratorDtailsService implements UserDetailsService {

    @Autowired
    AdministratorRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Administrator administrator = repository.findByName(username).orElse(null);
        if(administrator == null) {
            throw new UsernameNotFoundException("Username Not Found");
        }
        return new AdministratorDtailsImpl(administrator , new ArrayList<>());
    }
}
