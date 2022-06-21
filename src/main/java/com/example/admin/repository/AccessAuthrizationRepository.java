package com.example.admin.repository;


import com.example.admin.entity.AccessAuthorization;
import com.example.admin.entity.AccessAuthorizationPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccessAuthrizationRepository extends JpaRepository<AccessAuthorization , AccessAuthorizationPk> {

    //AccessAuthorization findByUriAndRoleId(String uri , Integer roleId);

}
