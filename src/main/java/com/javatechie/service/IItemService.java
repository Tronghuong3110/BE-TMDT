package com.javatechie.service;


import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.ProductDto;
import com.javatechie.dto.VariationDto;
import org.json.simple.JSONObject;

public interface IItemService {
    JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto);
    JSONObject updateProduct(ProductDto productDto, VariationDto variationDto, int categoryId);

}
