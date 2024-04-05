package com.javatechie.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {
    private Integer id;
    private String name;
    private String description;
    private String code;
    List<ItemDto> items = new ArrayList<>();
}
