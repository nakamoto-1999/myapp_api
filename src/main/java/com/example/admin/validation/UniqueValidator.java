package com.example.admin.validation;

import com.example.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique , String> {

    @Autowired
    private AdminService userService;

    @Override
    public void initialize(Unique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return !userService.isExistEmail(auth, value);
    }
}
