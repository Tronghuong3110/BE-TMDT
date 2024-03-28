package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ImageDto {
    private Integer id;
    private String path;
}
