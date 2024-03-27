package com.javatechie.entity;

import com.fasterxml.jackson.annotation.JacksonInject;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.sql.Date;

@Table(name = "orders")
@Data
@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "shipping_price")
    private Integer shippingPrice;
    @Column(name = "promotion_money")
    private Double promotionMoney;
    @Column(name = "status") // trạng thái thanh toán
    private String status;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;
    @OneToOne
    @JoinColumn(name = "shipment_id")
    private ShipmentEntity shipment;
}
