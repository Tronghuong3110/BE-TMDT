package com.javatechie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "accessory")
public class AccessoryEntity extends ItemEntity{
    @Column(name = "weight")
    private String weight;
    @Column(name = "connect_type")
    private String connectType; // kiểu kết nối của phụ kiện
    @Column(name = "special_properties")
    private String specialProperties;
}
