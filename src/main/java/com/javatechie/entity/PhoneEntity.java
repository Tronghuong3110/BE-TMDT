package com.javatechie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "phone")
@Data
public class PhoneEntity extends ItemEntity{
    @Column(name = "cpu")
    private String cpu;
    @Column(name = "frequency_screen")
    private String frequencyScreen;
    @Column(name = "front_camera")
    private String frontCamera;
    @Column(name = "rear_camera")
    private String rearCamera;
    @Column(name = "sim_number")
    private String simNumber;
    @Column(name = "battery_capacity")
    private String batteryCapacity;
}
