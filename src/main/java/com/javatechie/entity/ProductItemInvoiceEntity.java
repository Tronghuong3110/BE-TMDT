package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_item_invoice")
public class ProductItemInvoiceEntity {

    @Id
    private Long id;
    private Double cost;
    private Double price;
    private Integer quantity;
    private Date importDate;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;
    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItemEntity productItem;
}
