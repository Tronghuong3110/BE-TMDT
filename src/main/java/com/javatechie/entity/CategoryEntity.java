package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "category")
@Data
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "code")
    private String code;
    private Integer deleted;

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> products;
    @OneToMany(mappedBy = "category")
    private List<VariationEntity> variations;
}
