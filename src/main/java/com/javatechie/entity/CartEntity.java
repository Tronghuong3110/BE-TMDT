package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ordered")
    private Boolean ordered;
    @Column(name = "unix_time")
    private Long unixTime;

    @OneToMany(mappedBy = "cart")
    private List<CartItemEntity> cartItems;
    @OneToMany(mappedBy = "cart")
    private List<OrderEntity> orders;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
