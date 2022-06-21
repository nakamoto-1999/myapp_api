package com.example.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AccessAuthorizationPk implements Serializable {

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "uri")
    private String uri;

    public static AccessAuthorizationPk getInstance(Integer roleId , String uri){
        return new AccessAuthorizationPk(roleId , uri);
    }

}
