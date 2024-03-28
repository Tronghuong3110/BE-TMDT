package com.javatechie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "laptop")
public class LapTopEntity extends ItemEntity{
    @Column(name = "cpu")
    private String cpu;
    @Column(name = "card")
    private String card;
    @Column(name = "frequency_screen")
    private String frequencyScreen;
    @Column(name = "os")
    private String os;
    @Column(name = "usb_number")
    private String usbNumber;
    @Column(name = "type_disk")
    private String typeDisk;
    @Column(name = "weight")
    private String weight;
    @Column(name = "battery_capacity")
    private String batteryCapacity;
}
