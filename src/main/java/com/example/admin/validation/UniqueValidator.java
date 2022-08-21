package com.example.admin.validation;

import com.example.admin.security.MyUserDetails;
import com.example.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique , String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(Unique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return !userService.isExistEmail(auth, value);
    }
}
