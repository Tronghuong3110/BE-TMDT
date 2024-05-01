package com.javatechie.service.impl;

import com.javatechie.dto.BrandDto;
import com.javatechie.entity.BrandEntity;
import com.javatechie.repository.BrandRepository;
import com.javatechie.service.IBrandService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IBrandServiceImol implements IBrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Override
    public JSONObject save(BrandDto brandDto) {
        JSONObject response = new JSONObject();
        try {
            BrandEntity brand = new BrandEntity();
            BeanUtils.copyProperties(brandDto, brand);
            brand = brandRepository.save(brand);
            BeanUtils.copyProperties(brand, brandDto);
            response.put("code", 1);
            response.put("brand", brandDto);
            response.put("message", "Save brand success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Save brand fail");
        }
        return response;
    }

    @Override
    public List<BrandDto> findAllBrand() {
        try {
            List<BrandEntity> listBrand = brandRepository.findAll();
            List<BrandDto> listResponse = new ArrayList<>();
            for(BrandEntity brand : listBrand) {
                BrandDto brandDto = new BrandDto();
                BeanUtils.copyProperties(brand, brandDto);
                listResponse.add(brandDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
