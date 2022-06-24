package com.example.admin.response;

import com.example.admin.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private String ip;
    private UserResponse user;
    private String content;
    private boolean isValid;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public PostResponse(Post post){
        this.postId = post.getPostId();
        this.ip = post.getIp();
        this.user = new UserResponse(post.getUser());
        this.content = post.getContent();
        this.isValid = post.isValid();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

}
