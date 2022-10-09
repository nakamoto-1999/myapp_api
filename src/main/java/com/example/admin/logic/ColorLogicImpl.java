package com.example.admin.logic;

import com.example.admin.entity.Color;
import com.example.admin.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ColorLogicImpl implements ColorLogic{

    @Autowired
    ColorRepository colorRepository;

    @Override
    public Color getEntityByColorId(Long colorId) {
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
