package com.javatechie.service;

import com.javatechie.dto.PromotionDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IPromotionService {

    JSONObject savePromotion(PromotionDto promotionDto);
    List<PromotionDto> findAll();
    JSONObject findOne(Integer id);
    JSONObject update(PromotionDto promotionDto);
    JSONObject delete(Integer id);
}
