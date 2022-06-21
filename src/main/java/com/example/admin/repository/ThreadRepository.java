package com.example.admin.repository;

import com.example.admin.entity.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Integer> {
}
