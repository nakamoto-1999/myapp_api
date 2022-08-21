package com.example.admin.repository;

import com.example.admin.entity.Post;
import com.example.admin.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



public interface PostRepository extends JpaRepository<Post , Long> {

    //@Transactional
    //@Modifying
    //@Query("DELETE FROM Post p WHERE p.user.userId = ?1")
    //void deleteAllByUserId(Integ userId);

    List<Thread> findAllByOrderByPostId();

    @Query("SELECT DISTINCT p FROM Post p WHERE p.user.userId = ?1 ORDER BY p.postId")
    List<Post> findAllByUserIdOrderByPostId(Long userId);

    @Query("SELECT DISTINCT p FROM Post p WHERE p.thread.threadId = ?1 ORDER BY p.postId")
    List<Post> findAllByThreadIdOrderByPostId(Long threadId);

}
