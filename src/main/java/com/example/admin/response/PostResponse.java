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

    private Integer postId;
    private String ip;
    private UserResponse user;
    private String content;
    private Boolean isValid;
    private Timestamp created_at;
    private Timestamp updated_at;

    public PostResponse(Post post){
        this.postId = post.getPostId();
        this.ip = post.getIp();
        this.user = new UserResponse(post.getUser());
        this.content = post.getContent();
        this.isValid = post.getIsValid();
        this.created_at = post.getCreatedAt();
        this.updated_at = post.getUpdatedAt();
    }

}
