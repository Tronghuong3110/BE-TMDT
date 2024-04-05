package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ItemDetailDto {
    private Integer id;
    private String color;
    private Boolean isAvailable;
    private Integer soldNumber;
    private Double price;
    private Integer quantity;
    private String screenSize;
    private String diskSize;
    private String ram;
}
