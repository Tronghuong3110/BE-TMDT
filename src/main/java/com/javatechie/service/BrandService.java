package com.javatechie.service;

import com.javatechie.dto.BrandDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface BrandService {
    JSONObject save(BrandDto brandDto);
    List<BrandDto> findAllBrand();
}
