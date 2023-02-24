package com.example.admin.repository;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //@Query(value = "select u from User u where u.ip = ?1")
    Optional<User> findByIpEquals(String ip);

}
