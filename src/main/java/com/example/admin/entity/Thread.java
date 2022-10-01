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

    @Id
    @Column(name = "thread_id" )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long threadId;

    @Column(name = "title")
    String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "ip")
    String ip;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Column(name = "created_at")
    Timestamp createdAt;

    @Column(name = "updated_at")
    Timestamp updatedAt;

    @OneToMany(mappedBy = "thread" , cascade = CascadeType.ALL , orphanRemoval = true)
    List<Post> posts;

}
