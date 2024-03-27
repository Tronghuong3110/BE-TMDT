package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name = "import_invoice")
public class ImportInvoiceEntity {
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "import_date")
    private Date importDate;

    @OneToMany(mappedBy = "invoice")
    List<ItemInvoiceEntity> itemInvoices;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;
}
