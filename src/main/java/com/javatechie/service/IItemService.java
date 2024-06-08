package com.javatechie.service;


import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.ProductDto;
import com.javatechie.dto.ProductItemDto;
import com.javatechie.dto.VariationDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public interface IItemService {
    JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto, Integer brandId);

    JSONObject updateProductDetail(Long productItemId, VariationDto variationDto);

    JSONObject findOneById(Long productId, boolean isFindAll);

    JSONArray findAll(Integer categoryId, Integer brandId, String key);

    JSONObject deleteProduct(Long productId);

    JSONObject updateProduct(ProductDto productDto, Integer brandId);

    JSONObject deleteItemDetail(Long productItemId);

    JSONObject saveItemFavorite(Long id);

    List<JSONObject> findAllItemFavoriteOrViewed(Integer favorite, Integer viewed);

    List<JSONObject> importItem(List<ProductItemDto> productItems, Long supplierId);
}
