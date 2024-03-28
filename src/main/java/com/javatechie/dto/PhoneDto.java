package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PhoneDto extends ItemDetailDto{
    private String cpu;
    private String frequencyScreen;
    private String frontCamera;
    private String rearCamera;
    private Integer simNumber;
    private String batteryCapacity;
}
