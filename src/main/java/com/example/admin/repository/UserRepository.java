package com.example.admin.repository;

import com.example.admin.entity.User;
import com.example.admin.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String email);

}
