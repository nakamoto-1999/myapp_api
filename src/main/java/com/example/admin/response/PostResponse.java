package com.example.admin.response;

import com.example.admin.entity.Post;
import com.example.admin.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class PostResponse {

    private boolean isDeleted;
    private Long postId;
    private String ip;
    private UserResponse user = new UserResponse();
    private ColorResponse color;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PostResponse(Post post){
        //投稿が削除または書き込んだユーザー、書き込み先スレッドが削除されている場合
        isDeleted = post.isDeleted() || post.getUser().isDeleted() || post.getThread().isDeleted();
        createdAt = post.getCreatedAt();
        updatedAt = post.getUpdatedAt();
        postId = post.getPostId();
        if(!isDeleted) {
            ip = post.getIp();
            user = new UserResponse(post.getUser());
            color = new ColorResponse(post.getColor());
            content = post.getContent();
            return;
        }
    }

}
