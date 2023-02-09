package com.example.admin.logic;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;
import com.example.admin.utility.TimestampUtil;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

public class UserLogicImpl implements UserLogic{

    @Autowired
    UserRepository repository;

    @Autowired
    TimestampUtil timestampUtil;

    @Override
    public User getEntityByReq(@NotNull HttpServletRequest req) {

        //IPに紐づいたユーザーが存在する場合、そのユーザーを返す
        User user = repository.findByIp(req.getRemoteAddr()).orElse(null);
        if(user != null){
            return user;
        }

        //IPに紐づいたユーザーが存在しない場合、新しく作る
        user = new User();
        user.setIp(req.getRemoteAddr());
        user.setDeleted(false);
        user.setCreatedAt(timestampUtil.getNow());
        return repository.save(user);

    }

    @Override
    public User getEntityByUserId(Long userId) {
        User user = repository.findById(userId).orElseThrow(()->new IllegalArgumentException(""));
        return user;
    }

    @Override
    public User getEntityByIp(String ip) {
        User user = repository.findByIp(ip).orElseThrow(()->new IllegalArgumentException(""));
        return user;
    }
}
