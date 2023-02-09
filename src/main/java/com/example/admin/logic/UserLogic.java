package com.example.admin.logic;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public interface UserLogic {

    User getEntityByReq(HttpServletRequest req);
    User getEntityByUserId(Long userId);
    User getEntityByIp(String ip);

}
