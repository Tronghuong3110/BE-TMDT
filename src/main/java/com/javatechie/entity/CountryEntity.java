package com.javatechie.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "country")
public class CountryEntity {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "country")
    private List<AddressEntity> addresses;
}
