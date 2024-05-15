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
@Table(name = "variation_option")
public class VariationOptionEntity {
    @Id
    private Long id;
    private String value;

    @ManyToMany(mappedBy = "variationOptions")
    private List<ProductItemEntity> productItems;

    @ManyToOne
    @JoinColumn(name = "variation_id")
    private VariationEntity variation;
}
