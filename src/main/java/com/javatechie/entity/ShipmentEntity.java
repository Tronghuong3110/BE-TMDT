package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Table(name = "shipment")
@Data
@Entity
public class ShipmentEntity {
//
    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "price")
    private Integer price;
    @Column(name = "shipping_unit")
    private String shippingUnit; // đơn vị vận chuyển
    private String code;

    @OneToMany(mappedBy = "shipment")
    private List<OrderEntity> order;
}
