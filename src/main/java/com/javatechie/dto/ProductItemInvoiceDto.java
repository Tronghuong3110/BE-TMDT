package com.javatechie.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ProductItemInvoiceDto {

    private Long id;
    private Date importDate;
    private Double cost;
    private Double price;
    private Integer quantity;
    private ProductItemDto product;
}
