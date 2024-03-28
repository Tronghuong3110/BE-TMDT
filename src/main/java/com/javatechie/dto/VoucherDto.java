package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.security.PrivateKey;

@Data
public class VoucherDto {
    private Integer id;
    private String name;
    private String description;
    private Integer discount;
    private Integer discountConditions;
    private Integer numberVoucher;
    private Integer deleted;
}
