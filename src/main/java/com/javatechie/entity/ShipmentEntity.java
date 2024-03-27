package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "shipment")
@Data
@Entity
public class ShipmentEntity {
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "price")
    private Integer price;
    @Column(name = "shipping_unit")
    private String shippingUnit; // đơn vị vận chuyển

    @OneToOne(mappedBy = "shipment")
    private OrderEntity order;
}
