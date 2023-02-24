package com.example.admin.repository;

import com.example.admin.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ThreadRepository extends JpaRepository<Thread, Long> {

    List<Thread> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT th FROM Thread th WHERE th.user.userId = ?1 ORDER BY th.threadId")
    List<Thread> findAllByUserIdOrderByThreadId(Long userId);

    @Query("SELECT DISTINCT th FROM Thread th WHERE th.overview LIKE %?1% OR th.point LIKE %?1% OR th.red LIKE %?1% OR th.blue LIKE %?1% " +
            "ORDER BY th.createdAt DESC ")
    List<Thread> findAllByKeyword(String keyword);

    //レス数が1000以上、レス数が0かつスレッド作成後の60分経過、最新のレス投稿後365日経過しているスレッドのis_deletedを1にする
    @Query(
            nativeQuery = true,
            value = "UPDATE thread SET thread.is_closed = 1 " +
                    "WHERE thread.is_closed = 0 AND ( " +
                        "( SELECT COUNT(post.post_id) FROM post WHERE post.thread_id = thread.thread_id ) >= 1000 OR " +
                        "DATEDIFF(NOW() , thread.created_at)  >= 365" +
                    ")"
    )
    @Modifying
    void updateIsClosed();

    //閉鎖されていないスレッドを集計する
    @Query(value = "SELECT COUNT(th) FROM Thread th WHERE th.isClosed = 0")
    Long countNotClosedThreads();

}
