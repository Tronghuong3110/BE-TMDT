package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_item")
public class ProductItemEntity {

    @Id
    private Long id;
    private Integer quantityInStock;
    private Integer quantitySold;
    private Double price;
    private Integer deleted;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToMany(mappedBy = "productItems", fetch = FetchType.LAZY)
    private List<VariationOptionEntity> variationOptions;

    @ManyToMany
    @JoinTable(name = "product_promotion",
    joinColumns = @JoinColumn(name = "product_item_id"),
    inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private List<PromotionEntity> promotions;

    @OneToMany(mappedBy = "productItem")
    private List<ProductItemInvoiceEntity> productItemInvoices;

    @OneToMany(mappedBy = "productItem")
    private List<CartItemEntity> cartItem;
}
