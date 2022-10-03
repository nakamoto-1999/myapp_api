package com.example.admin.response;

import com.example.admin.entity.Thread;
import com.example.admin.entity.User;
import com.example.admin.utility.ThreadStopperUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    @JsonFormat(pattern = "yyyy年M月d日H時m分")
    private Timestamp finishAt;

    public ThreadResponse(Thread thread){
        isDeleted = thread.isDeleted() || thread.getUser().isDeleted();
        threadId = thread.getThreadId();
        createdAt = thread.getCreatedAt();
        updatedAt = thread.getUpdatedAt();
        if(!isDeleted) {
            title = thread.getTitle();
            user = new UserResponse(thread.getUser());
            //デフォルトでは、スレッドのcreatedAtを基準としたスレッド終了時刻を取得する
            finishAt = getFinishAt(thread.getCreatedAt());
            return;
        }
    }

    public void setPosts(List<PostResponse> posts) {
        if(isDeleted){return;}
        this.posts = posts;
        //レス数が0でなければ、最新のレスのcreatedAtを基準としたスレッド終了時刻を取得する
        Integer postNum = this.posts.size();
        if(postNum >= 1){
            finishAt = getFinishAt(this.posts.get(postNum - 1).getCreatedAt());
        }
    }

    public Timestamp getFinishAt(Timestamp timestamp){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());
        cal.add(Calendar.MINUTE , 60);
        return new Timestamp(cal.getTime().getTime());
    }

}
