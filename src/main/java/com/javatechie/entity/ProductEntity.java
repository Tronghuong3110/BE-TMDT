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
@Table(name = "product")
public class ProductEntity {
    @Id
    private Long id;
    private String name;
    private Boolean deleted;

    @OneToMany(mappedBy = "product")
    private List<ProductItemEntity> productItems;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;
    @OneToMany(mappedBy = "product")
    private List<ImageEntity> images;
    @OneToMany(mappedBy = "product")
    private List<ReviewEntity> reviews;
    @OneToMany(mappedBy = "product")
    private List<CommentEntity> comments;
}
