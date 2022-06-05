package com.example.admin.service;

import com.example.admin.entity.Administrator;
import com.example.admin.response.AdministratorResponse;

import java.util.List;

public interface AdministratiorService {

    void create(String name , String password);
    List<AdministratorResponse> getAllResponse();
    AdministratorResponse getResponseByAdminId(Integer adminId);
    AdministratorResponse getResponseByName(String name);
    Administrator getEntityByAdminId(Integer adminId);
    Administrator getEntityByName(String name);
    void update(Integer adminId ,  String name , String password);
    void delete(Integer adminId);

}
