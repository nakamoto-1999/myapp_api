package com.example.admin.repository;

import com.example.admin.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color , Long> {
}
