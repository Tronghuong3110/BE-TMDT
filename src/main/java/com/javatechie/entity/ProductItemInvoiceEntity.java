package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "product_item_invoice")
public class ProductItemInvoiceEntity {

    @Id
    private Long id;
    @Column(name = "import_date")
    private Date importDate;
    private Double cost;
    private Double price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;
    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItemEntity productItem;
}
