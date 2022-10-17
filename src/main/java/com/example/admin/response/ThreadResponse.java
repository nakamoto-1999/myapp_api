package com.example.admin.response;

import com.example.admin.entity.BlockedUserOfThread;
import com.example.admin.entity.Post;
import com.example.admin.entity.Thread;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
public class ThreadResponse {

    private boolean isDeleted;
    private Long threadId;
    private String overview;
    private String point;
    private String red;
    private String blue;
    private boolean isClosed;
    private boolean isConcluded;
    private ColorResponse concludedColor;
    private String conclusionReason;
    private List<PostResponse> posts = new ArrayList<>();
    private List<UserResponse> blockedUsers = new ArrayList<>();
    private UserResponse user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @JsonFormat(pattern = "yyyy年M月d日H時m分")
    private Timestamp finishAt;

    public ThreadResponse(Thread thread){

        if(thread == null)return;
        isDeleted = thread.isDeleted() || thread.getUser().isDeleted();
        threadId = thread.getThreadId();
        createdAt = thread.getCreatedAt();
        updatedAt = thread.getUpdatedAt();
        if(isDeleted)return;
        overview = thread.getOverview();
        point = thread.getPoint();
        red = thread.getRed();
        blue = thread.getBlue();
        isClosed = thread.isClosed();
        if(thread.isConcluded() && thread.getConcludedColor() != null){
            isConcluded = thread.isConcluded();
            concludedColor = new ColorResponse(thread.getConcludedColor());
            conclusionReason = thread.getConclusionReason();
        }
        user = new UserResponse(thread.getUser());
        //デフォルトでは、スレッドのcreatedAtを基準としたスレッド終了時刻を取得する
        finishAt = getFinishAt(thread.getCreatedAt());

    }

    public void setPosts(List<Post> posts){
        if(isDeleted)return;
        posts.forEach(post -> {
            //System.out.println(post.getContent());
            this.posts.add(new PostResponse(post));
        });
        //this.posts.forEach(postResponse -> {
        //    System.out.println(postResponse.getContent());
        //});
    }

    public void setBlockedUsers(List<BlockedUserOfThread> blockedUsers){
        if(isDeleted)return;
        blockedUsers.forEach(blockedUserOfThread -> {
            //System.out.println(blockedUserOfThread.getUser().getName());
            this.blockedUsers.add(new UserResponse(blockedUserOfThread.getUser()));
        });
        //this.blockedUsers.forEach(blockedUserResponse -> {
        //    System.out.println(blockedUserResponse.user.getName());
        //});
    }

    private Timestamp getFinishAt(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.HOUR , 12);
        return new Timestamp(cal.getTime().getTime());
    }

}
