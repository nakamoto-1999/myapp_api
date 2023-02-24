package com.example.admin.logic;

import com.example.admin.entity.User;
import com.example.admin.repository.UserRepository;
import com.example.admin.utility.TimestampUtil;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@Component
public class UserLogicImpl implements UserLogic{

    @Autowired
    UserRepository repository;

    @Autowired
    TimestampUtil timestampUtil;

    @Override
    public User createUser(@NotNull HttpServletRequest req) {

        //IPに紐づいたユーザーが存在しない場合、新しく作る
        User user = new User();
        user.setIp(req.getRemoteAddr());
        user.setPermitted(true);
        user.setDeleted(false);
        user.setCreatedAt(timestampUtil.getNow());
        return repository.save(user);

    }

    @Override
    public boolean isUserExistByIp(String ip) {
        return repository.findByIpEquals(ip).orElse(null) != null;
    }

    @Override
    public User getEntityByUserId(Long userId) {
        User user = repository.findById(userId).orElseThrow(()->new IllegalArgumentException(""));
        return user;
    }

    @Override
    public User getEntityByIp(String ip) {
        User user = repository.findByIpEquals(ip).orElseThrow(()->new IllegalArgumentException(""));
        return user;
    }
}
