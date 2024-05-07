package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SupplierDto {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
}
