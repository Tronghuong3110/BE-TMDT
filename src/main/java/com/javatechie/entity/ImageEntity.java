package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "path", columnDefinition = "text")
    private String path;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
