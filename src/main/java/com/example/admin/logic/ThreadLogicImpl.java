package com.example.admin.logic;

import com.example.admin.entity.Thread;
import com.example.admin.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThreadLogicImpl implements ThreadLogic {

    @Autowired
    ThreadRepository threadRepository;

    @Override
    public Thread getEntityByThreadId(Long threadId) {
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(()->new IllegalArgumentException());
        return thread;
    }
}
