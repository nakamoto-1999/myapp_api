package com.example.admin.entity;

import com.example.admin.compositekey.PkOfThreadAndUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "blocked_user_of_thread")
public class BlockedUserOfThread {

    @EmbeddedId
    PkOfThreadAndUser pk;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thread_id")
    Thread thread;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    public BlockedUserOfThread(Long threadId , Long userId , Thread thread , User user){
        this.pk = new PkOfThreadAndUser(threadId , userId);
        this.thread = thread;
        this.user = user;
    }

}
