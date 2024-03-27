package com.javatechie.service;

import com.javatechie.dto.CategoryDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface ICategoryService {

    JSONObject saveCategory(CategoryDto categoryDto);
    List<CategoryDto> findAll();
    JSONObject findOneById(Integer id);
    JSONObject updateCategory(CategoryDto category);
    JSONObject deleteCategory(Integer id);
}
