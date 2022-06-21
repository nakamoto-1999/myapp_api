package com.example.admin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "thread")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Thread {

    @Column(name = "thread_id")
    @Id
    Integer threadId;

    @Column(name = "title")
    String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "thread" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Post> posts;

    @Column(name = "created_at")
    Timestamp createdAt;

    @Column(name = "updated_at")
    Timestamp updatedAt;

}
