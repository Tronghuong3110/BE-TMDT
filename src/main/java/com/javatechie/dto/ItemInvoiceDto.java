package com.javatechie.dto;

import com.javatechie.entity.ImportInvoiceEntity;
import com.javatechie.entity.ItemDetailEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ItemInvoiceDto {
    private Long id;
    private Integer quantity;
    private Integer importPrice;
    private ItemDto item;
    private ImportInvoiceDto invoice;
}
