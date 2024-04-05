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
    private Integer id;
    @Column(name = "vendor")
    private String vendor; // nhà sản xuất
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "deleted")
    private Integer deleted;

    @OneToMany(mappedBy = "item")
    private List<ItemDetailEntity> itemDetails;
    @OneToMany(mappedBy = "itemEntity")
    private List<ImageEntity> images;
    @OneToMany(mappedBy = "item")
    private List<ReviewEntity> reviews;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
