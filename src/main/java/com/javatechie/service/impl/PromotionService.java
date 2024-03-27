package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.PromotionDto;
import com.javatechie.entity.*;
import com.javatechie.repository.ItemDetailRepository;
import com.javatechie.repository.ItemRepository;
import com.javatechie.repository.PromotionRepository;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.IPromotionService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService implements IPromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemDetailRepository itemDetailRepository;

    @Override
    public JSONObject savePromotion(PromotionDto promotionDto) {
        JSONObject response = new JSONObject();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
            if(user == null || (!userDetails.getRoles().contains("EMPLOYEE") && !userDetails.getRoles().contains("ADMIN"))) {
                response.put("code", 0);
                response.put("message", "Permission denied");
                return response;
            }
            PromotionEntity promotion = new PromotionEntity();
            BeanUtils.copyProperties(promotionDto, promotion);
            promotion.setDeleted(0);
            promotion = promotionRepository.save(promotion);
            // chọn sản phầm áp dụng khuyến mại
            if(promotionDto.getIdItems() != null) {
                for(Integer idItem : promotionDto.getIdItems()) {
                    ItemDetailEntity item = itemDetailRepository.findByIdAndIsAvailable(idItem, true).orElse(null);
                    if(item == null) {
                        continue;
                    }
                    item.setPromotion(promotion);
                    itemDetailRepository.save(item);
                }
            }
            response.put("code", 1);
            response.put("message", "Add new promotion success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new promotion fail");
        }
        return response;
    }

    @Override
    public List<PromotionDto> findAll() {
        try {
            List<PromotionEntity> listPromotions = promotionRepository.findAllByDeleted(0);
            List<PromotionDto> listResponse = new ArrayList<>();
            for(PromotionEntity promotion : listPromotions) {
                PromotionDto promotionDto = new PromotionDto();
                BeanUtils.copyProperties(promotion, promotionDto);
                listResponse.add(promotionDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject findOne(Integer id) {
        JSONObject response = new JSONObject();
        try {
            PromotionEntity promotion = promotionRepository.findByIdAndDeleted(id, 0).orElse(null);
            if(promotion == null) {
                response.put("code", 0);
                response.put("message", "Can not found promotion with id = " + id);
                return response;
            }
            PromotionDto promotionDto = new PromotionDto();
            BeanUtils.copyProperties(promotion, promotionDto);
            response.put("code", 1);
            response.put("message", promotionDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Can not found");
        }
        return response;
    }

    @Override
    public JSONObject update(PromotionDto promotionDto) {
        JSONObject response = new JSONObject();
        try {
            PromotionEntity promotion = promotionRepository.findByIdAndDeleted(promotionDto.getId(), 0).orElse(null);
            if(promotion == null) {
                response.put("code", 0);
                response.put("message", "Can not found promotion with id = " + promotionDto.getId());
                return response;
            }
            promotion = toEntity(promotionDto, promotion);
            if(promotion == null) {
                response.put("code", 0);
                response.put("message", "Can not update promotion");
                return response;
            }

            // cập nhật lại danh sách sản phẩm được áp dụng khuyến mại
            promotion = promotionRepository.save(promotion);
            if(promotionDto.getIdItems() != null) {
                for(Integer idItem : promotionDto.getIdItems()) {
                    ItemDetailEntity item = itemDetailRepository.findByIdAndIsAvailable(idItem, true).orElse(null);
                    if(item == null) {
                        continue;
                    }
                    item.setPromotion(promotion);
                    itemDetailRepository.save(item);
                }
            }
            response.put("code", 1);
            response.put("message", "Update promotion success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Update promotion fail");
        }
        return response;
    }

    @Override
    public JSONObject delete(Integer id) {
        JSONObject response = new JSONObject();
        try {
            PromotionEntity promotion = promotionRepository.findById(id).orElse(null);
            if(promotion == null) {
                response.put("code", 0);
                response.put("message", "Can not found promotion with id = " + id);
                return response;
            }
            promotion.setDeleted(1);
            promotionRepository.save(promotion);
            response.put("code", 1);
            response.put("message", "Delete promotion success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Delete promotion fail");
        }
        return response;
    }

    private PromotionEntity toEntity(PromotionDto promotionDto, PromotionEntity promotion) {
        try {
            if(promotionDto.getContent() != null) {
                promotion.setContent(promotionDto.getContent());
            }
            if(promotionDto.getDateStart() != null) {
                promotion.setDateStart(promotionDto.getDateStart());
            }
            if (promotionDto.getDateEnd() != null) {
                promotion.setDateEnd(promotionDto.getDateEnd());
            }
            if(promotionDto.getDiscount() != null) {
                promotion.setDiscount(promotionDto.getDiscount());
            }
            return promotion;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
