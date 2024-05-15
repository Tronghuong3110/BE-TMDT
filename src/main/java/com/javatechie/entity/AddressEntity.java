package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    private Long id;
    @Column(name = "unit_number")
    private String unitNumber;
    @Column(name = "street_number")
    private String streetNumber;
    @Column(name = "city")
    private String city;
    @Column(name = "region")
    private String region;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;
    @OneToOne(mappedBy = "address")
    private SupplierEntity supplier;

    @OneToMany(mappedBy = "address")
    private List<UserAddressEntity> userAddresses;
}
