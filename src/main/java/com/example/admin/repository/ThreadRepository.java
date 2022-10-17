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

    //レス数が1000以上、レス数が0かつスレッド作成後の60分経過、最新のレス投稿後60分経過しているスレッドのis_deletedを1にする
    @Query(
            nativeQuery = true,
            value = "UPDATE thread SET thread.is_closed = 1 " +
                    "WHERE thread.is_closed = 0 AND ( " +
                        "( SELECT COUNT(post.post_id) FROM post WHERE post.thread_id = thread.thread_id ) >= 1000 OR " +
                        "TIMEDIFF(NOW() , thread.created_at)  >= \"12:00:00\" " +
                    ")"
    )
    @Modifying
    void updateIsClosed();

    //削除されていないスレッドを集計する
    @Query(value = "SELECT COUNT(th) FROM Thread th WHERE th.isDeleted = 0")
    Long countNotDeletedThreads();

}
