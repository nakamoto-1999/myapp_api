package com.example.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "color")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Color {

    @Column(name = "color_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long colorId;

    @Column(name = "name")
    String name;

}
