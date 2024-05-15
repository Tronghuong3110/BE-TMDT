package com.javatechie.service.impl;

import com.javatechie.dto.VariationDto;
import com.javatechie.entity.CategoryEntity;
import com.javatechie.entity.VariationEntity;
import com.javatechie.repository.CategoryRepository;
import com.javatechie.repository.VariationOptionRepository;
import com.javatechie.repository.VariationRepository;
import com.javatechie.service.IVariationService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VariationService implements IVariationService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VariationRepository variationRepository;
    @Autowired
    private VariationOptionRepository variationOptionRepository;

    @Override
    public JSONObject saveVariation(VariationDto variationDto, int categoryId) {
        JSONObject response = new JSONObject();
        try {
            CategoryEntity category = categoryRepository.findById(categoryId).orElse(new CategoryEntity());
            VariationEntity variation = new VariationEntity();
            BeanUtils.copyProperties(variationDto, variation);
            variation.setCategory(category);
            variation.setId(System.currentTimeMillis());
            variation = variationRepository.save(variation);
            BeanUtils.copyProperties(variation, variationDto);
            response.put("code", 1);
            response.put("message", "Add new variation success");
            response.put("variation", variationDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new variation fail");
            response.put("variation", new VariationEntity());
        }
        return response;
    }

    @Override
    public JSONObject updateVariation(VariationDto variationDto, int categoryId) {
        JSONObject response = new JSONObject();
        try {
            VariationEntity variation = variationRepository.findById(variationDto.getId()).orElse(new VariationEntity());
            variation = toEntity(variation, variationDto);
            if(variation.getCategory().getId() != categoryId) {
                CategoryEntity category = categoryRepository.findById(categoryId).orElse(new CategoryEntity());
                variation.setCategory(category);
            }
            variation = variationRepository.save(variation);
            BeanUtils.copyProperties(variation, variationDto);
            response.put("code", 1);
            response.put("message", "Update variation success");
            response.put("variation", variationDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Update variation fail");
            response.put("variation", new VariationDto());
        }
        return response;
    }

    @Override
    public List<VariationDto> findAllByCategory(int categoryId) {
        try {
            List<VariationEntity> variations = variationRepository.findAllByCategory_Id(categoryId);
            List<VariationDto> responses = new ArrayList<>();
            for(VariationEntity variation : variations) {
                VariationDto variationDto = new VariationDto();
                BeanUtils.copyProperties(variation, variationDto);
                responses.add(variationDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public VariationDto findOneVariation(int id) {
        return null;
    }

    private VariationEntity toEntity(VariationEntity variation, VariationDto variationDto) {
        if(variationDto.getName() != null) {
            variation.setName(variationDto.getName());
        }
        return variation;
    }
}
