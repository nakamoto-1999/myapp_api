package com.example.admin.compositekey;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PkOfThreadAndUser implements Serializable {

    @Column(name = "thread_id")
    Long threadId;

    @Column(name = "user_id")
    Long userId;

}
