package com.javatechie.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestImportItem {
    private List<ProductItemDto> productItems;
    private Long supplierId;
}
