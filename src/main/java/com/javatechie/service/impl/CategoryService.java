package com.javatechie.service.impl;

import com.javatechie.dto.CategoryDto;
import com.javatechie.entity.Category;
import com.javatechie.repository.CategoryRepository;
import com.javatechie.service.ICategoryService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public JSONObject saveCategory(CategoryDto categoryDto) {
        JSONObject response = new JSONObject();
        try {
            Category category = new Category();
            BeanUtils.copyProperties(categoryDto, category);
            Boolean checkExistCode = categoryRepository.existsByName(categoryDto.getName());
            if(checkExistCode) {
                response.put("code", 0);
                response.put("message", "Code has been duplicated");
                return response;
            }
            categoryRepository.save(category);
            response.put("code", 1);
            response.put("message", "Add category success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @Override
    public List<CategoryDto> findAll() {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDto> listResponse = new ArrayList<>();
            for(Category category : categories) {
                CategoryDto categoryDto = new CategoryDto();
                BeanUtils.copyProperties(category, categoryDto);
                listResponse.add(categoryDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject findOneById(Integer id) {
        JSONObject response = new JSONObject();
        try {
            Category category = categoryRepository.findById(id).orElse(null);
            if(category == null) {
                response.put("code", 0);
                response.put("message", "Can not found category with id = " + id);
                return response;
            }
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            response.put("code", 1);
            response.put("message", categoryDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", null);
        }
        return response;
    }

    @Override
    public JSONObject updateCategory(CategoryDto categoryDto) {
        JSONObject response = new JSONObject();
        try {
            Category category = categoryRepository.findById(categoryDto.getId()).orElse(null);
            if(category == null) {
                response.put("code", 0);
                response.put("message", "Can not found category with id = " + categoryDto.getId());
            }
            category = convertToEntity(category, categoryDto);
            if(category == null) {
                response.put("code", 0);
                response.put("message", "Can not change information of category with id = " + categoryDto.getId());
                return response;
            }
            categoryRepository.save(category);
            response.put("code", 1);
            response.put("message", "Update category success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Update category fail");
        }
        return response;
    }

    @Override
    public JSONObject deleteCategory(Integer id) {
        JSONObject response = new JSONObject();
        try {
            categoryRepository.deleteById(id);
            response.put("code", 1);
            response.put("message", "Delete category success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Delete category fail");
        }
        return response;
    }

    private Category convertToEntity(Category category, CategoryDto categoryDto) {
        try {
            if(categoryDto.getName() != null) {
                category.setName(category.getName());
            }
            if(categoryDto.getDescription() != null) {
                category.setDescription(categoryDto.getDescription());
            }
            return category;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
