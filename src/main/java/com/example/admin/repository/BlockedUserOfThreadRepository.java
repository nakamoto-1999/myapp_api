package com.example.admin.repository;

import com.example.admin.compositekey.PkOfThreadAndUser;
import com.example.admin.entity.BlockedUserOfThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedUserOfThreadRepository extends JpaRepository<BlockedUserOfThread , PkOfThreadAndUser> {
}
