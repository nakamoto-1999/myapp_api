package com.example.admin.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "access_authorization")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccessAuthorization {

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @Column(name = "uri")
    String uri;

}
