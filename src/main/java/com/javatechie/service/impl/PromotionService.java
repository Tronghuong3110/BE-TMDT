package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.ProductItemDto;
import com.javatechie.dto.PromotionDto;
import com.javatechie.entity.*;
import com.javatechie.repository.ProductItemRepository;
import com.javatechie.repository.PromotionRepository;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.IPromotionService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService implements IPromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private ItemService itemService;

    @Override
    public JSONObject savePromotion(PromotionDto promotionDto) {
        JSONObject response = new JSONObject();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
            if(user == null || (!userDetails.getRoles().contains("EMPLOYEE") && !userDetails.getRoles().contains("ADMIN"))) {
                response.put("code", 0);
                response.put("message", "Bạn không có quyền thêm mới khuyến mại !!");
                return response;
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            PromotionEntity promotion = new PromotionEntity();
            mapper.map(promotionDto, promotion);
            promotion.setDeleted(0);
            promotion = promotionRepository.save(promotion);
            List<PromotionEntity> listPromotion = new ArrayList<>();
            listPromotion.add(promotion);
            List<JSONArray> listItem = new ArrayList<>();
            // chọn sản phầm áp dụng khuyến mại
            if(promotionDto.getIdItems() != null) {
                JSONParser parser = new JSONParser();
                for(Long idItem : promotionDto.getIdItems()) {
                    ProductItemEntity productItem = productItemRepository.findByIdAndDeleted(idItem, 0).orElse(null);
                    if(productItem == null) continue;
                    productItem.setPromotions(listPromotion);
                    productItemRepository.save(productItem);
                    JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(idItem);
                    if(object == null) continue;
                    JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
                    JSONObject quantity = new JSONObject();
                    quantity.put("quantity_stock", productItem.getQuantityInStock());
                    quantity.put("quantity_sold", productItem.getQuantitySold());
                    quantity.put("productItemId", productItem.getId());
                    quantity.put("price", productItem.getPrice());
                    productDetail.add(quantity);
                    listItem.add(productDetail);
                }
            }
            mapper.map(promotion, promotionDto);
            response.put("code", 1);
            response.put("message", "Thêm mới khuyến mại thành công !!");
            response.put("promotion", promotionDto);
            response.put("listItem", listItem);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới khuyến mại thất bại !!");
            response.put("promotion", new ArrayList<>());
            response.put("listItem", new ArrayList<>());
        }
        return response;
    }

    @Override
    public List<PromotionDto> findAll() {
        try {
            List<PromotionEntity> listPromotions = promotionRepository.findAllByDeleted(0);
            List<PromotionDto> listResponse = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(PromotionEntity promotion : listPromotions) {
                PromotionDto promotionDto = new PromotionDto();
                mapper.map(promotion, promotionDto);
                List<JSONObject> listProduct = new ArrayList<>();
                for(ProductItemEntity productItem : promotion.getProductItems()) {
                    JSONObject product = itemService.findOneById(productItem.getId(), true);
                    listProduct.add(product);
                }
                promotionDto.setProducts(listProduct);
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
                response.put("message", "Không thể tìm thấy khuyến mại với id = " + id);
                return response;
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            PromotionDto promotionDto = new PromotionDto();
            mapper.map(promotion, promotionDto);
            List<JSONObject> listProduct = new ArrayList<>();
            for(ProductItemEntity productItem : promotion.getProductItems()) {
                JSONObject product = itemService.findOneById(productItem.getId(), true);
                listProduct.add(product);
            }
            promotionDto.setProducts(listProduct);
            response.put("code", 1);
            response.put("message", promotionDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Không thể tìm thấy khuyến mại !!");
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
                response.put("message", "Không thể tìm thấy khuyến mại");
                return response;
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(promotionDto, promotion);
            promotion.setDeleted(0);
            if(promotion == null) {
                response.put("code", 0);
                response.put("message", "Không thể cập nhật khuyến mại !!");
                return response;
            }

            // cập nhật lại danh sách sản phẩm được áp dụng khuyến mại
            promotion = promotionRepository.save(promotion);
            List<PromotionEntity> listPromotion = new ArrayList<>();
            listPromotion.add(promotion);
            List<JSONArray> listItem = new ArrayList<>();
            // cập nhật thêm sản phẩm được áp dụng khuyến mại
            if(promotionDto.getIdItems() != null) {
                JSONParser parser = new JSONParser();
                for(Long idItem : promotionDto.getIdItems()) {
                    ProductItemEntity productItem = productItemRepository.findByIdAndDeleted(idItem, 0).orElse(null);
                    if(productItem == null) continue;
                    productItem.setPromotions(listPromotion);
                    productItemRepository.save(productItem);
                    JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(productItem.getId());
                    if(object == null) continue;
                    JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
                    JSONObject quantity = new JSONObject();
                    quantity.put("quantity_stock", productItem.getQuantityInStock());
                    quantity.put("quantity_sold", productItem.getQuantitySold());
                    quantity.put("productItemId", productItem.getId());
                    quantity.put("price", productItem.getPrice());
                    productDetail.add(quantity);
                    listItem.add(productDetail);
                }
            }
            // loại bỏ khuyến mại của một vaài sản phẩm được yêu cầu
            if(promotionDto.getIdItemsRemove() != null) {
                for(Long idItem : promotionDto.getIdItemsRemove()) {
                    ProductItemEntity productItem = productItemRepository.findByIdAndDeleted(idItem, 0).orElse(null);
                    if(productItem == null) continue;
                    productItem.setPromotions(null);
                    productItemRepository.save(productItem);
                }
            }
            BeanUtils.copyProperties(promotion, promotionDto);
            response.put("code", 1);
            response.put("message", "Cập nhật khuyến mại thành công !!");
            response.put("listItem", listItem);
            response.put("promotion", promotionDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Cập nhật thông tin khuyến mại lỗi !!");
            response.put("listItem", new ArrayList<>());
            response.put("promotion", new PromotionDto());
        }
        return response;
    }

    @Override
    @Transactional
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
            productItemRepository.deleteProductItemPromotion(id);
            promotion.setDeleted(1);
            promotionRepository.save(promotion);
            response.put("code", 1);
            response.put("message", "Xóa thông tin khuyến mại thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa thông tin khuyến mại thất bại !!");
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
