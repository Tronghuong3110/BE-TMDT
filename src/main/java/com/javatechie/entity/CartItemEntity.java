package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.modelmapper.internal.bytebuddy.matcher.InheritedAnnotationMatcher;

@Data
@Table(name = "cartItem")
@Entity
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "ordered")
    private Integer ordered;
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemDetailEntity item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
