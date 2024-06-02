package com.javatechie.service.impl;

import com.javatechie.dto.BrandDto;
import com.javatechie.entity.BrandEntity;
import com.javatechie.repository.BrandRepository;
import com.javatechie.service.IBrandService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService implements IBrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public JSONObject save(BrandDto brandDto) {
        JSONObject response = new JSONObject();
        try {
            BrandEntity brand = new BrandEntity();
            BeanUtils.copyProperties(brandDto, brand);
            brand.setDeleted(0);
            brand = brandRepository.save(brand);
            BeanUtils.copyProperties(brand, brandDto);
            response.put("code", 1);
            response.put("brand", brandDto);
            response.put("message", "Thêm mới hãng sản xuất thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới hãng sản xuất thất bại !!");
        }
        return response;
    }

    @Override
    public List<BrandDto> findAllBrand() {
        try {
            List<BrandEntity> listBrand = brandRepository.findAllByDeleted(0);
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

    @Override
    public JSONObject findOneById(Integer brandId) {
        JSONObject response = new JSONObject();
        try {
            BrandEntity brand = brandRepository.findByIdAndDeleted(brandId, 0).orElse(null);
            if(brand == null) {
                response.put("code", 0);
                response.put("message", "Hãng sản xuất không tồn tại !!");
                return response;
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            BrandDto brandDto = new BrandDto();
            mapper.map(brand, brandDto);
            brandDto.setItems(null);
            response.put("code", 1);
            response.put("message", "Thành công !!");
            response.put("brand", brandDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Lấy thông tin hãng sản xuất thất bại !!");
            response.put("brand", null);
        }
        return response;
    }

    @Override
    public JSONObject deleteBrand(Integer brandId) {
        JSONObject response = new JSONObject();
        try {
            BrandEntity brand = brandRepository.findById(brandId).orElse(null);
            if(brand == null) {
                response.put("code", 0);
                response.put("message", "Hãng sản xuất không tồn tại !!");
                return response;
            }
            brand.setDeleted(1);
            brandRepository.save(brand);
            response.put("code", 1);
            response.put("message", "Xóa hãng sản xuất thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa hãng sản xuất thất bại !!");
        }
        return response;
    }

    @Override
    public JSONObject updateBrand(BrandDto brandDto) {
        JSONObject response = new JSONObject();
        try {
            BrandEntity brand = brandRepository.findByIdAndDeleted(brandDto.getId(), 0).orElse(null);
            if(brand == null) {
                response.put("code", 0);
                response.put("message", "Hãng sản xuất không tồn tại !!");
                return response;
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(brandDto, brand);
            brandRepository.save(brand);
            mapper.map(brand, brandDto);
            brandDto.setItems(null);
            response.put("code", 1);
            response.put("message", "Chỉnh sửa hãng sản xuất thành công !!");
            response.put("brand", brandDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Chỉnh sửa hãng sản xuất thất bại !!");
            response.put("brand", null);
        }
        return response;
    }
}
