package com.javatechie.dto;

import lombok.Data;

@Data
public class RequestUpdate {
    private ProductDto product;
    private VariationDto variation;
}
