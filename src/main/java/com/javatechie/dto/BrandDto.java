package com.javatechie.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrandDto {
    private Integer id;
    private String name;
    private String description;
    private List<ProductDto> items;
}
