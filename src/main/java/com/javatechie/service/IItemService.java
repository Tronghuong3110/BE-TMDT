package com.javatechie.service;


import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.ProductDto;
import com.javatechie.dto.VariationDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface IItemService {
    JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto, Integer brandId);
    JSONObject updateProductDetail(Long productItemId, VariationDto variationDto);
    JSONObject findOneById(Long productId, boolean isFindAll);
    JSONArray findAll(Integer categoryId, Integer brandId, String key);
    JSONObject deleteProduct(Long productId);
    JSONObject updateProduct(ProductDto productDto, Integer brandId);
}
