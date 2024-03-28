package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "item_detail")
public class ItemDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "color")
    private String color;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "sold_number")
    private Integer soldNumber;
    @Column(name = "price")
    private Double price;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "screen_size")
    private Float screenSize;
    @Column(name = "disk_size")
    private Integer diskSize;
    @Column(name = "ram")
    private Integer ram;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @OneToMany(mappedBy = "item")
    List<ItemInvoiceEntity> invoices;
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;
    @OneToOne(mappedBy = "item")
    private CartItemEntity cartItem;

}
