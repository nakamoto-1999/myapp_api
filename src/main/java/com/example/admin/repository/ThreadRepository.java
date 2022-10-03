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

    //レス数が1000以上、レス数が0かつスレッド作成後の60分経過、最新のレス投稿後60分経過しているスレッドのis_deletedを1にする
    @Query(
            nativeQuery = true,
            value = "UPDATE thread SET thread.is_deleted = 1 " +
                    "WHERE thread.is_deleted = 0 AND ( " +
                        "( SELECT COUNT(post.post_id) FROM post WHERE post.thread_id = thread.thread_id ) >= 1000 OR " +
                        "( SELECT COUNT(post.post_id) FROM post WHERE post.thread_id = thread.thread_id ) <= 0 AND " +
                        "TIMEDIFF( NOW() , thread.created_at ) >= \"00:60:00\" OR " +
                        "( SELECT TIMEDIFF(NOW() , post.created_at) FROM post " +
                            "WHERE post.thread_id = thread.thread_id ORDER BY post.created_at DESC LIMIT 1 ) >= \"00:60:00\" " +
                    ")"
    )
    @Modifying
    void updateIsDeletedOfFinishedThreads();

    //削除されていないスレッドを集計する
    @Query(value = "SELECT COUNT(th) FROM Thread th WHERE th.isDeleted = 0")
    Long countNotDeletedThreads();

}
