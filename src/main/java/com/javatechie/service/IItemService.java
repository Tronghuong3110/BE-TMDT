package com.javatechie.service;


import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.ProductDto;
import com.javatechie.dto.VariationDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface IItemService {
    JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto);
    JSONObject updateProduct(ProductDto productDto, VariationDto variationDto, Integer categoryId, Integer brandId);
    JSONObject findOneById(Long productId, boolean isFindAll);
    JSONArray findAll(Integer categoryId, Integer brandId, String key);
}
