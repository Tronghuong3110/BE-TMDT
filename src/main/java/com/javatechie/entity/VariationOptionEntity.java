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
    private Long unixTime;


    @ManyToMany
    @JoinTable(name = "product_variation",
            joinColumns = @JoinColumn(name = "variation_option_id"),
            inverseJoinColumns = @JoinColumn(name = "product_item_id"))
    private List<ProductItemEntity> productItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id")
    private VariationEntity variation;
}
