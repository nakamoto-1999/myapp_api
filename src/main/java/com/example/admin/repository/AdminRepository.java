package com.example.admin.repository;

import com.example.admin.entity.Admin;
import com.example.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;



public interface AdminRepository extends JpaRepository<Admin, Long> {

    List<Admin> findAllByOrderByAdminId();

    //有効なユーザーの中で、同一のメールアドレスが存在するかを調べる
    @Query("select a from Admin a where a.email = ?1")
    Optional<Admin> findByEmail(String email);

}
