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

    @OneToMany(mappedBy = "product")
    private List<ProductItemEntity> productItems;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @OneToMany(mappedBy = "product")
    private List<ImageEntity> images;
    @OneToMany(mappedBy = "product")
    private List<ReviewEntity> reviews;
}
