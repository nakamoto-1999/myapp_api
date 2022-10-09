package com.example.admin.repository;

import com.example.admin.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ThreadRepository extends JpaRepository<Thread, Long> {

    List<Thread> findAllByOrderByThreadId();

    @Query("SELECT DISTINCT th FROM Thread th WHERE th.user.userId = ?1 ORDER BY th.threadId")
    List<Thread> findAllByUserIdOrderByThreadId(Long userId);

    //削除されていないスレッドを集計する
    @Query(value = "SELECT COUNT(th) FROM Thread th WHERE th.isDeleted = 0")
    Long countNotDeletedThreads();

}
