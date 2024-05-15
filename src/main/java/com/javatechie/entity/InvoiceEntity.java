package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "invoice")
public class InvoiceEntity {

    @Id
    private Long id;
    @Column(name = "create_date")
    private Date createDate;

    @OneToMany(mappedBy = "invoice")
    private List<ProductItemInvoiceEntity> productItemInvoices;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;
}
