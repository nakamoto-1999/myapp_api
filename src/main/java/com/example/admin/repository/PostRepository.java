package com.example.admin.repository;

import com.example.admin.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



public interface PostRepository extends JpaRepository<Post , Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p.user.userId = ?1")
    void deleteAllByUserId(Integer userId);

    @Query("SELECT DISTINCT p FROM Post p WHERE p.postId = ?1")
    List<Post> findAllByUserId(Integer userId);

}
