package com.example.admin.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class AdministratorResponse {

    private Integer adminId;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
