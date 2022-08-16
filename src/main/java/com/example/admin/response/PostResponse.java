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
        //投稿そのものが無効、または書き込んだユーザーが無効の場合、それをフロント側に知らせるフラグ以外はレスポンスを返さない
        if(post.isValid() && post.getUser().isValid()) {
            postId = post.getPostId();
            ip = post.getIp();
            user = new UserResponse(post.getUser());
            isValid = true;
            content = post.getContent();
            createdAt = post.getCreatedAt();
            updatedAt = post.getUpdatedAt();
        }
        else{
            isValid = false;
        }
    }

}
