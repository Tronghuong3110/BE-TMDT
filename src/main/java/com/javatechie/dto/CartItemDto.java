package com.javatechie.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartItemDto {
    private Integer id;
    private Integer quantity;
    private Double price; //
    private Long productItemId;
    private Long productId;
    private List<ImageDto> images;
}
