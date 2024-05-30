package com.javatechie.dto;

import lombok.Data;

import java.util.List;

@Data
public class VariationDto {
    private Long id;
    private String name;
    private String variationOptionValue;
    private List<VariationOptionDto> variationOptions;
}
