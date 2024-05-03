package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.ItemDto;
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
import org.springframework.context.annotation.Bean;
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
                    ItemEntity item = itemRepository.findByIdAndDeleted(idItem, 0).orElse(null);
                    if(item == null) {
                        continue;
                    }
                    item.setPromotion(promotion);
                    itemRepository.save(item);
                }
            }
            BeanUtils.copyProperties(promotion, promotionDto);
            response.put("code", 1);
            response.put("message", "Add new promotion success");
            response.put("promotion", promotionDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new promotion fail");
            response.put("promotion", null);
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
                List<ItemEntity> listItem = promotion.getItems();
                List<ItemDto> listItemDto = new ArrayList<>();
                for(ItemEntity item : listItem) {
                    ItemDto itemDto = new ItemDto();
                    BeanUtils.copyProperties(item, itemDto);
                    listItemDto.add(itemDto);
                }
                promotionDto.setItems(listItemDto);
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
            List<ItemEntity> listItem = promotion.getItems();
            List<ItemDto> listItemDto = new ArrayList<>();
            for(ItemEntity item : listItem) {
                ItemDto itemDto = new ItemDto();
                BeanUtils.copyProperties(item, itemDto);
                listItemDto.add(itemDto);
            }
            promotionDto.setItems(listItemDto);
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
            promotion.setDeleted(0);
            if(promotion == null) {
                response.put("code", 0);
                response.put("message", "Can not update promotion");
                return response;
            }

            // cập nhật lại danh sách sản phẩm được áp dụng khuyến mại
            promotion = promotionRepository.save(promotion);
            List<ItemDto> listResponse = new ArrayList<>();
            // cập nhật thêm sản phẩm được áp dụng khuyến mại
            if(promotionDto.getIdItems() != null) {
                for(Integer idItem : promotionDto.getIdItems()) {
                    ItemEntity item = itemRepository.findByIdAndDeleted(idItem, 0).orElse(null);
                    if(item == null) {
                        continue;
                    }
                    item.setPromotion(promotion);
                    item = itemRepository.save(item);
                    ItemDto itemDto= new ItemDto();
                    BeanUtils.copyProperties(item, itemDto);
                    listResponse.add(itemDto);
                }
            }
            // loại bỏ khuyến mại của một vaài sản phẩm được yêu cầu
            if(promotionDto.getIdItemsRemove() != null) {
                for(Integer idItem : promotionDto.getIdItemsRemove()) {
                    ItemEntity item = itemRepository.findByIdAndDeleted(idItem, 0).orElse(null);
                    if(item == null) continue;
                    item.setPromotion(null);
                    itemRepository.save(item);
                }
            }
            BeanUtils.copyProperties(promotion, promotionDto);
            response.put("code", 1);
            response.put("message", "Update promotion success");
            response.put("listItem", listResponse);
            response.put("promotiom", promotionDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Update promotion fail");
            response.put("listItem", new ArrayList<>());
            response.put("promotiom", new PromotionDto());
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
            // xóa bỏ khuyến mại đang xóa được áp dụng trên các sản phẩm
            List<ItemEntity> listItems = itemRepository.findAllByDeletedAndPromotion_id(0, promotion.getId());
            for(ItemEntity item : listItems) {
                item.setPromotion(null);
                itemRepository.save(item);
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
            BeanUtils.copyProperties(promotionDto, promotion);
            return promotion;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
