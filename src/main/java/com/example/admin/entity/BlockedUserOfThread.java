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

    @ManyToOne()
    @JoinColumn(name = "thread_id" , insertable = false , updatable = false)
    Thread thread;

    @ManyToOne()
    @JoinColumn(name = "user_id" , insertable = false , updatable = false)
    User user;

    public BlockedUserOfThread(Thread thread , User user){
        this.pk = new PkOfThreadAndUser(thread.getThreadId() , user.getUserId());
        this.thread = thread;
        this.user = user;
    }

}
