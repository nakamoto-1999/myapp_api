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

    private Long threadId;
    private String title;
    private List<PostResponse> posts = new ArrayList<>();
    private UserResponse user;
    private boolean isValid;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ThreadResponse(Thread thread){
        //データベース上のスレッドが無効、スレッドを立てたユーザーが無効の場合、スレッドがDAT落ちしている場合
        // スレッドの無効を知らせるフラグ以外はフロントに返さない
        if(thread.isValid() && thread.getUser().isValid() ) {
            threadId = thread.getThreadId();
            title = thread.getTitle();
            user = new UserResponse(thread.getUser());
            isValid = true;
            createdAt = thread.getCreatedAt();
            updatedAt = thread.getUpdatedAt();
        }
        else {
            isValid = false;
        }
    }

    public void setPosts(List<PostResponse> posts) {
        if(!isValid){return;}
        this.posts = posts;
    }
}
