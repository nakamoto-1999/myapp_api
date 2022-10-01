package com.example.admin.repository;

import com.example.admin.entity.User;
import com.example.admin.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;



public interface UserRepository extends JpaRepository<User , Long> {

    List<User> findAllByOrderByUserId();

    //削除されていないユーザーの中で、同一のメールアドレスが存在するかを調べる
    @Query("select u from User u where u.isDeleted = false and u.email = ?1")
    Optional<User> findByEmail(String email);

}
