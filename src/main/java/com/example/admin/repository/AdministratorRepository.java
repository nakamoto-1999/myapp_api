package com.example.admin.repository;

import com.example.admin.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator , Integer> {

    Optional<Administrator> findByName(String email);

}
