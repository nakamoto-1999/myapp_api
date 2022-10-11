package com.example.admin.response;

import com.example.admin.entity.BlockedUserOfThread;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class BlockedUserResponse {

    Long threadId;
    UserResponse user;

    public BlockedUserResponse(@NotNull BlockedUserOfThread blockedUser){
        threadId = blockedUser.getThread().getThreadId();
        user = new UserResponse(blockedUser.getUser());
    }

}
