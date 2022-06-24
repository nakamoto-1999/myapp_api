package com.example.admin.response;

import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Data
public class ThreadResponse {

    private Long threadId;
    private String title;
    private List<PostResponse> posts;
    private UserResponse user;
    private boolean isValid;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ThreadResponse(Thread thread){
        this.threadId = thread.getThreadId();
        this.title = thread.getTitle();
        this.user = new UserResponse(thread.getUser());
        this.isValid = thread.isValid();
        this.createdAt = thread.getCreatedAt();
        this.updatedAt = thread.getUpdatedAt();
    }

}
