package com.example.admin.service;

import com.example.admin.entity.Administrator;
import com.example.admin.entity.Post;
import com.example.admin.repository.AdministratorRepository;
import com.example.admin.repository.PostRepository;
import com.example.admin.response.AdministratorResponse;
import com.example.admin.response.PostResponse;
import com.example.admin.utility.TimestampManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AdministratorServiceImpl implements AdministratiorService{

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    TimestampManager timestampManager;

    @Override
    public void create(String name, String password) {

        Administrator administrator = new Administrator();
        administrator.setName(name);
        administrator.setPassword(password);
        administrator.setCreatedAt(timestampManager.getNow());
        administratorRepository.save(administrator);

    }

    @Override
    public List<AdministratorResponse> getAllResponse() {
        List<Administrator> administrators = administratorRepository.findAll();
        List<AdministratorResponse> administratorResponses = new ArrayList<>();
        //管理者情報をレスポンス用リストに格納する
        for(Administrator administrator : administrators){
            administratorResponses.add(this.getResponse(administrator));
        }
        return administratorResponses;
    }

    @Override
    public AdministratorResponse getResponseByAdminId(Integer id) {
        Administrator administrator = this.getEntityByAdminId(id);
        return this.getResponse(administrator);
    }

    private AdministratorResponse getResponse(Administrator administrator){
        return new AdministratorResponse(
                administrator.getAdminId(),
                administrator.getName(),
                administrator.getCreatedAt(),
                administrator.getUpdatedAt()
        );
    }

    @Override
    public AdministratorResponse getResponseByName(String name) {
        Administrator administrator = this.getEntityByName(name);
        return new AdministratorResponse(
                administrator.getAdminId(),
                administrator.getName(),
                administrator.getCreatedAt(),
                administrator.getUpdatedAt()
        );
    }

    @Override
    public Administrator getEntityByAdminId(Integer adminId) {
        Administrator administrator = administratorRepository.findById(adminId)
                .orElseThrow(()->new InvalidConfigurationPropertyValueException("Administrator Not Found" , adminId , "Administrator Not Found By AdminID"));
        return administrator;
    }

    @Override
    public Administrator getEntityByName(String name) {
        Administrator administrator = administratorRepository.findByName(name)
                .orElseThrow(()->new InvalidConfigurationPropertyValueException("Administrator Not Found" ,name , "Administrator Not Found By Name"));
        return administrator;
    }

    @Override
    public void update(Integer adminId, String name ,String password) {
        Administrator administrator = this.getEntityByAdminId(adminId);
        administrator.setName(name);
        administrator.setPassword(password);
        administrator.setUpdatedAt(timestampManager.getNow());
        administratorRepository.save(administrator);
    }

    @Override
    public void delete(Integer adminId) {
        Administrator administrator = this.getEntityByAdminId(adminId);
        administratorRepository.delete(administrator);
    }
}
