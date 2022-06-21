package com.example.admin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "access_authorization")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessAuthorization implements Serializable {

    @EmbeddedId
    AccessAuthorizationPk key;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id" , insertable = false , updatable = false)
    Role role;

}
