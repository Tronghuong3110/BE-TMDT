package com.javatechie.service.impl;

import com.javatechie.dto.CategoryDto;
import com.javatechie.entity.CategoryEntity;
import com.javatechie.repository.CategoryRepository;
import com.javatechie.service.ICategoryService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
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
            CategoryEntity categoryEntity = new CategoryEntity();
            BeanUtils.copyProperties(categoryDto, categoryEntity);
            Boolean checkExistCode = categoryRepository.existsByName(categoryDto.getName());
            if(checkExistCode) {
                response.put("code", 0);
                response.put("message", "Code has been duplicated");
                return response;
            }
            categoryEntity = categoryRepository.save(categoryEntity);
            BeanUtils.copyProperties(categoryEntity, categoryDto);
            response.put("code", 1);
            response.put("message", "Thêm mới thể loại thành công !!");
            response.put("category", categoryDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới thể loại thất bại");
            response.put("category", null);
        }
        return response;
    }

    @Override
    public List<CategoryDto> findAll() {
        try {
            List<CategoryEntity> categories = categoryRepository.findAll();
            List<CategoryDto> listResponse = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(CategoryEntity categoryEntity : categories) {
                CategoryDto categoryDto = new CategoryDto();
                mapper.map(categoryEntity, categoryDto);
                categoryDto = setVariationOptionIsNull(categoryDto);
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
        ModelMapper mapper = MapperUtil.configModelMapper();
        try {
            CategoryEntity categoryEntity = categoryRepository.findByIdAndDeleted(id, 0).orElse(null);
            if(categoryEntity == null) {
                response.put("code", 0);
                response.put("message", "Thể loại không tồn tại !!");
                return response;
            }
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(categoryEntity, categoryDto);
            categoryDto = setVariationOptionIsNull(categoryDto);
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
            CategoryEntity categoryEntity = categoryRepository.findById(categoryDto.getId()).orElse(null);
            if(categoryEntity == null) {
                response.put("code", 0);
                response.put("message", "Không tìm thấy thể loại !!");
            }
            categoryEntity = convertToEntity(categoryEntity, categoryDto);
            if(categoryEntity == null) {
                response.put("code", 0);
                response.put("message", "Không thể thay đổi thông tin thể loại !!");
                return response;
            }
            categoryRepository.save(categoryEntity);
            response.put("code", 1);
            response.put("message", "Cập nhật thông tin thể loại thành công");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thông tin thể loại thất bại !!");
        }
        return response;
    }

    @Override
    public JSONObject deleteCategory(Integer id) {
        JSONObject response = new JSONObject();
        try {
            CategoryEntity category = categoryRepository.findById(id).orElse(null);
            if(category == null) {
                response.put("code", 0);
                response.put("message", "Thể loại không tồn tại !!");
                return response;
            }
            category.setDeleted(1);
            response.put("code", 1);
            response.put("message", "Xóa thể loại thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa thể loại thất bại !!");
        }
        return response;
    }

    private CategoryEntity convertToEntity(CategoryEntity categoryEntity, CategoryDto categoryDto) {
        try {
            BeanUtils.copyProperties(categoryDto, categoryEntity);
            return categoryEntity;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private CategoryDto setVariationOptionIsNull(CategoryDto categoryDto) {
        categoryDto.getVariations().forEach(variation -> {variation.setVariationOptions(null);});
        return categoryDto;
    }
}
