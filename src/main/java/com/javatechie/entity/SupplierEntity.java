package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "supplier")
@Data
@Entity
public class SupplierEntity {

    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "phonenumber")
    private String phoneNumber;
    @Column(name = "deleted")
    private Integer deleted;

    @OneToMany(mappedBy = "supplier")
    private List<ImportInvoiceEntity> invoices;
}
