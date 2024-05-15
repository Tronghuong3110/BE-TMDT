package com.javatechie.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductItemDto {
    private Long id;
    private String sku;
    private List<VariationOptionDto> variationOptions;
}
