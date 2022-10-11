package com.example.admin.response;

import com.example.admin.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColorResponse {

    Long colorId;
    String name;

    public ColorResponse(Color color){
        if(color == null)return;
        colorId = color.getColorId();
        name = color.getName();
    }

}
