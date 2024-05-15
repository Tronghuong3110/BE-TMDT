package com.javatechie.dto;

import lombok.Data;

@Data
public class CartItemDto {
    private Integer id;
    private Integer quantity;
    private Double price; //
    private ProductDto productDto;
}
