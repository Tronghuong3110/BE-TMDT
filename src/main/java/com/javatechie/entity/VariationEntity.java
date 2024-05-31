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
@Table(name = "variation")
public class VariationEntity {

    @Id
    private Long id;
    private String name;
    private String nameVie;

    @OneToMany(mappedBy = "variation", fetch = FetchType.LAZY)
    private List<VariationOptionEntity> variationOptions;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
}
