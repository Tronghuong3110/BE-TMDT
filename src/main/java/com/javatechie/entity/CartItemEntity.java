package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "cartItem")
@Entity
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "quantity")
    private Integer quantity;
//
    @OneToOne
    @JoinColumn(name = "item_id")
    private ItemDetailEntity item;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;
}
