package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "cart")
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "unix_time")
    private Long unixTime;

    @OneToMany(mappedBy = "cart")
    private List<CartItemEntity> cartItems;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
