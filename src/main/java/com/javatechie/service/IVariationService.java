package com.javatechie.service;

import com.javatechie.dto.VariationDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IVariationService {
    JSONObject saveVariation(VariationDto variationDto, int categoryId);
    JSONObject updateVariation(VariationDto variationDto, int categoryId);
    List<VariationDto> findAllByCategory(int categoryId);
    VariationDto findOneVariation(int id);
}
