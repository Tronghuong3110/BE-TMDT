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
    private String sku;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToMany
    @JoinTable(name = "product_variation",
    joinColumns = @JoinColumn(name = "product_item_id"),
    inverseJoinColumns = @JoinColumn(name = "variation_option_id"))
    private List<VariationOptionEntity> variationOptions;

    @ManyToMany
    @JoinTable(name = "product_promotion",
    joinColumns = @JoinColumn(name = "product_item_id"),
    inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    private List<PromotionEntity> promotions;

    @ManyToMany
    @JoinTable(name = "invoice_product",
        joinColumns = @JoinColumn(name = "product_item_id"),
        inverseJoinColumns = @JoinColumn(name = "invoice_id"))
    private List<ItemInvoiceEntity> invoices;

    @OneToMany(mappedBy = "productItem")
    private List<ProductItemInvoiceEntity> productItemInvoices;

    @OneToOne(mappedBy = "productItem")
    private CartItemEntity cartItem;
}
