package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ShipmentDto {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String shippingUnit;
}
