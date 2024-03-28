package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class LapTopDto extends ItemDetailDto{
    private String cpu;
    private String card;
    private String frequencyScreen;
    private String os;
    private String usbNumber;
    private String typeDisk;
    private String weight;
    private String batteryCapacity;
}
