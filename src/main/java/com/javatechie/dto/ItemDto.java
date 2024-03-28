package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private Integer id;
    private String vendor; // nhà sản xuất
    private String name;
    private String description;
    List<ImageDto> images;
    List<ItemDetailDto> itemDetails;

}
