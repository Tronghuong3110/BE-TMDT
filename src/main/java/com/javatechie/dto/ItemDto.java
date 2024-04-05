package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private Integer id;
    private String vendor; // nhà sản xuất
    private String name;
    private String description;
    // thông tin của laptop
    private String cpu;
    private String card;
    private String frequencyScreen;
    private String os;
    private String usbNumber;
    private String typeDisk;
    private String weight;
    private String batteryCapacity;
    // thông tin của điện thoại
    private String frontCamera;
    private String rearCamera;
    private String simNumber;
    // thông tin của phụ kiện
    private String connectType; // kiểu kết nối của phụ kiện
    private String specialProperties;
    // danh sách ảnh của sản phẩm
    List<ImageDto> images;
    // danh sách thông tin chi tiết của sản phẩm
    List<ItemDetailDto> itemDetails;
    
}
