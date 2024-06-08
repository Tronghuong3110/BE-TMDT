package com.javatechie.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private Long productItemId;
    private String description;
    private List<ProductItemDto> productItems;
    private CategoryDto category;
    private List<ImageDto> images;
    private List<ReviewDto> reviews;
    private List<CommentDto> comments;

}
