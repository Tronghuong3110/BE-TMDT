package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "item")
@Entity
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(name = "vendor")
    protected String vendor; // nhà sản xuất
    @Column(name = "name")
    protected String name;
    @Column(name = "description", columnDefinition = "longtext")
    protected String description;
    @Column(name = "deleted")
    protected Integer deleted;

    @OneToMany(mappedBy = "item")
    protected List<ItemDetailEntity> itemDetails;
    @OneToMany(mappedBy = "itemEntity")
    protected List<ImageEntity> images;
    @OneToMany(mappedBy = "item")
    protected List<ReviewEntity> reviews;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    protected BrandEntity brand;
    @ManyToOne
    @JoinColumn(name = "category_id")
    protected CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    protected PromotionEntity promotion;
}
