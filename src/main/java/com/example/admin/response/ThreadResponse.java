package com.example.admin.response;

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
    private String overView;
    private String point;
    private List<PostResponse> posts = new ArrayList<>();
    private UserResponse user = new UserResponse();
    private Timestamp createdAt;
    private Timestamp updatedAt;
    @JsonFormat(pattern = "yyyy年M月d日H時m分")
    private Timestamp finishAt;

    public ThreadResponse(Thread thread){
        isDeleted = thread.isDeleted() || thread.getUser().isDeleted();
        threadId = thread.getThreadId();
        createdAt = thread.getCreatedAt();
        updatedAt = thread.getUpdatedAt();
        if(!isDeleted) {
            overView = thread.getOverview();
            point = thread.getPoint();
            user = new UserResponse(thread.getUser());
            //デフォルトでは、スレッドのcreatedAtを基準としたスレッド終了時刻を取得する
            finishAt = getFinishAt(thread.getCreatedAt());
        }
    }

    public void setPosts(List<PostResponse> posts){
        if(!isDeleted)this.posts = posts;
    }

    private Timestamp getFinishAt(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.MINUTE , 60);
        return new Timestamp(cal.getTime().getTime());
    }

}
