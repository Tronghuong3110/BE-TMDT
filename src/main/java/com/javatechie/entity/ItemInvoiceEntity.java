package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "item_invoice")
public class ItemInvoiceEntity {

    @Id
    private Long id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "import_price")
    private Integer importPrice;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemDetailEntity item;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private ImportInvoiceEntity invoice;
}
