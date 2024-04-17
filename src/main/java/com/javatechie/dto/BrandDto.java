package com.javatechie.dto;

import com.javatechie.entity.ItemEntity;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class BrandDto {
    private Integer id;
    private String name;
    private String description;
    private List<ItemDto> items;
}
