package com.example.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long adminId;

    @Column(name = "name")
    String name;

    @Column(name = "emal")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @Column(name = "createdAt")
    Timestamp createdAt;

    @Column(name = "updatedAt")
    Timestamp updatedAt;

}
