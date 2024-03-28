package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AccessoryDto extends ItemDetailDto{
    private String weight;
    private String connectType; // kiểu kết nối của phụ kiện
    private String specialProperties;
}
