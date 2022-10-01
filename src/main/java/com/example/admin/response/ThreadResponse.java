package com.example.admin.response;

import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.utility.ThreadStopperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class ThreadResponse {

    private boolean isDeleted;
    private Long threadId;
    private String title;
    private List<PostResponse> posts = new ArrayList<>();
    private UserResponse user = new UserResponse();
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ThreadResponse(Thread thread){
        isDeleted = thread.isDeleted() || thread.getUser().isDeleted();
        if(!isDeleted) {
            threadId = thread.getThreadId();
            title = thread.getTitle();
            user = new UserResponse(thread.getUser());
            createdAt = thread.getCreatedAt();
            updatedAt = thread.getUpdatedAt();
            return;
        }
    }

    public void setPosts(List<PostResponse> posts) {
        if(!isDeleted){return;}
        this.posts = posts;
    }
}
