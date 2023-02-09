package com.example.admin.response;

import com.example.admin.entity.Post;
import com.example.admin.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
        if(post == null)return;
        isDeleted = post.isDeleted() || post.getThread().isDeleted();
        createdAt = post.getCreatedAt();
        updatedAt = post.getUpdatedAt();
        postId = post.getPostId();
        if(isDeleted)return;
        user = new UserResponse(post.getUser());
        color = new ColorResponse(post.getColor());
        content = post.getContent();
    }

}
