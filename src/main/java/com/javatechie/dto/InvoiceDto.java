package com.javatechie.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class InvoiceDto {
    private Long id;
    private Date createDate;
    private SupplierDto supplier;
    private List<ProductItemInvoiceDto> products;
    private Double totalPrice;
}
