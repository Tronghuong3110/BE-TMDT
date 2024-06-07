package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "payment")
@Data
public class PaymentEntity {

    @Id
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "longtext")
    private String description;
    private String code;
    @OneToMany(mappedBy = "payment")
    private List<OrderEntity> order;
}
