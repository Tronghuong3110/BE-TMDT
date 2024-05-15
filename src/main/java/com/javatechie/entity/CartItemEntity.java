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
    @OneToOne
    @JoinColumn(name = "product_item_id")
    private ProductItemEntity productItem;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
