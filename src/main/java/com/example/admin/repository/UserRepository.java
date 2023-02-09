package com.example.admin.repository;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIp(String ip);

}
