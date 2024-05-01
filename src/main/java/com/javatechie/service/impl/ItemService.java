package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.*;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IItemService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService implements IItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemDetailRepository itemDetailRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ItemViewedRepository itemViewedRepository;
    @Override
    public JSONObject saveItem(ItemDto item, Integer categoryId, Integer brandId) {
        JSONObject response = new JSONObject();
        CategoryEntity category = categoryRepository.findById(categoryId).orElse(null);
        BrandEntity brand = brandRepository.findById(brandId).orElse(null);
        try {
            if(category == null || brand == null) {
                response.put("code", 0);
                response.put("message", "Can not found category and brand");
                return response;
            }
            // thêm mới điện thoại
            if(category.getCode().equals("dien-thoai")) {
                PhoneEntity phone = saveNewItem(item, category, brand, new PhoneEntity());
                if(phone == null) {
                    response.put("code", 0);
                    response.put("message", "Add new item error");
                    return response;
                }
                for(ImageDto imageDto : item.getImages()) {
                    ImageEntity image = new ImageEntity();
                    BeanUtils.copyProperties(imageDto, image);
                    image.setItemEntity(phone);
                    imageRepository.save(image);
                }
                BeanUtils.copyProperties(phone, item);
                response.put("message", "Add new phone success");
            }
            else if (category.getCode().equals("lap-top")) {
                LapTopEntity lapTop = saveNewItem(item, category, brand, new LapTopEntity());
                if(lapTop == null) {
                    response.put("code", 0);
                    response.put("message", "Add new item error");
                    return response;
                }
                for(ImageDto imageDto : item.getImages()) {
                    ImageEntity image = new ImageEntity();
                    BeanUtils.copyProperties(imageDto, image);
                    image.setItemEntity(lapTop);
                    imageRepository.save(image);
                }
                response.put("message", "Add new laptop success");
                BeanUtils.copyProperties(lapTop, item);
            }
            else if (category.getCode().equals("phu-kien")) {
                AccessoryEntity accessory = saveNewItem(item, category, brand, new AccessoryEntity());
                if(accessory == null) {
                    response.put("code", 0);
                    response.put("message", "Add new item error");
                    return response;
                }
                for(ImageDto imageDto : item.getImages()) {
                    ImageEntity image = new ImageEntity();
                    BeanUtils.copyProperties(imageDto, image);
                    image.setItemEntity(accessory);
                    imageRepository.save(image);
                }
                response.put("message", "Add new accessory success");
                BeanUtils.copyProperties(accessory, item);
            }
            response.put("item", item);
            response.put("code", 1);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new item error");
            response.put("item", null);
        }
        return response;
    }

    @Override
    public JSONObject updateItem(ItemDto item, Integer categoryId, Integer brandId) {
        JSONObject response = new JSONObject();
        try {
            CategoryEntity category = categoryRepository.findById(categoryId).orElse(null);
            BrandEntity brand = brandRepository.findById(brandId).orElse(null);
            if(category == null || brand == null) {
                response.put("code", 0);
                response.put("message", "Can not update item because of can not found category and brand");
                return response;
            }
            ItemEntity itemEntity = itemRepository.findByIdAndDeleted(item.getId(), 0).orElse(new ItemEntity());
            if(itemEntity == null) {
                response.put("code", 0);
                response.put("message", "Can not found item with id = " + item.getId());
                return response;
            }
            itemEntity = updateItem(item, category, brand, itemEntity);
            BeanUtils.copyProperties(itemEntity, item);
//            CategoryDto categoryDto = new CategoryDto();
//            ItemDetailDto itemDetailDto = new ItemDetailDto();
//            BeanUtils.copyProperties(itemEntity.getCategory(), categoryDto);
            response.put("code", 1);
            response.put("message", "Update item success");
            response.put("item", item);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Can not update item");
        }
        return response;
    }

    @Override
    public JSONObject deleteItem(Integer id) {
        JSONObject response = new JSONObject();
        try {
            ItemEntity item = itemRepository.findById(id).orElse(null);
            if(item == null) {
                response.put("code", 0);
                response.put("message", "Can not found item with id = " + item.getId());
                return response;
            }
            item.setDeleted(1);
            itemRepository.save(item);
            response.put("code", 1);
            response.put("message", "Delete item success");
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Can not delete item");
        }
        return response;
    }

    @Override
    public ItemDto findOneById(Integer id) {
        try {
            ItemEntity item = itemRepository.findByIdAndDeleted(id, 0).orElse(null);
            if(item == null) {
                return null;
            }
            ItemDto itemDto = new ItemDto();
            BeanUtils.copyProperties(item, itemDto);
            List<ItemDetailEntity> listItemDetail = itemDetailRepository.findAllByItem_IdAndDeleted(id, 0);
            List<ItemDetailDto> listItemDetailDto = new ArrayList<>();
            for(ItemDetailEntity itemDetailEntity : listItemDetail) {
                ItemDetailDto itemDetailDto = new ItemDetailDto();
                BeanUtils.copyProperties(itemDetailEntity, itemDetailDto);
                listItemDetailDto.add(itemDetailDto);
            }
            itemDto.setItemDetails(listItemDetailDto);
            List<ImageEntity> listImage = imageRepository.findAllByItemEntity(item);
            List<ImageDto> listImageDto = new ArrayList<>();
            for(ImageEntity image : listImage) {
                ImageDto imageDto = new ImageDto();
                BeanUtils.copyProperties(image, imageDto);
                listImageDto.add(imageDto);
            }
            itemDto.setImages(listImageDto);
            CategoryDto category = new CategoryDto();
            BeanUtils.copyProperties(item.getCategory(), category);
            itemDto.setCategoryDto(category);
            // thêm mới vào danh sách item đã xem của user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object object = auth.getPrincipal();
            if(!(object.equals("anonymousUser"))) {
                // TH đã đăng nhập
                UserInfoUserDetails userDetails = new UserInfoUserDetails();
                User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
                ItemViewedEntity itemViewed = itemViewedRepository.findByItemIdAndUserId(item.getId(), user.getId()).orElse(
                        new ItemViewedEntity(System.currentTimeMillis(), item.getId(), user.getId(), 1, 0));
                itemViewed.setViewed(1);
                itemViewedRepository.save(itemViewed);
            }
            return itemDto;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ItemDto> findAllItem(Integer categoryId, Integer brandId, String key) {
        List<ItemEntity> listItem = itemRepository.findAllByDeletedAndCategory_IdAndBrand_IdAndKey(categoryId, brandId, key);
        List<ItemDto> listResponse = new ArrayList<>();
        try {
            for(ItemEntity item : listItem) {
                System.out.println();
                ItemDto itemDto = new ItemDto();
                BeanUtils.copyProperties(item, itemDto);
                List<ItemDetailEntity> itemDetailEntities = item.getItemDetails();
                List<ItemDetailDto> listItemDetail = new ArrayList<>();
                for(ItemDetailEntity itemDetail : itemDetailEntities) {
                    ItemDetailDto itemDetailDto = new ItemDetailDto();
                    BeanUtils.copyProperties(itemDetail, itemDetailDto);
                    listItemDetail.add(itemDetailDto);
                }
                itemDto.setItemDetails(listItemDetail);
                List<ImageDto> listImage = new ArrayList<>();
                for(ImageEntity image : item.getImages()) {
                    ImageDto imageDto = new ImageDto();
                    BeanUtils.copyProperties(image, imageDto);
                    listImage.add(imageDto);
                }
                itemDto.setImages(listImage);
                CategoryDto category = new CategoryDto();
                BeanUtils.copyProperties(item.getCategory(), category);
                itemDto.setCategoryDto(category);
                listResponse.add(itemDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // thêm mới sản phẩm yêu thích
    @Override
    public JSONObject saveItemFavorite(Integer id) {
        JSONObject response = new JSONObject();
        try {
            ItemEntity item = itemRepository.findByIdAndDeleted(id, 0).orElse(new ItemEntity());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            ItemViewedEntity itemViewedEntity = new ItemViewedEntity(System.currentTimeMillis(), item.getId(), user.getId(), 0, 1);
            itemViewedEntity = itemViewedRepository.save(itemViewedEntity);
            response.put("code", 1);
            response.put("message", "Add new success");
            response.put("itemView", itemViewedEntity.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new fail");
            response.put("itemView", null);
        }
        return response;
    }

    @Override
    public List<ItemDto> findAllItemFavoriteOrViewed(Integer favorite, Integer viewed) {
        try {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            List<ItemViewedEntity> listItemViewed = itemViewedRepository.findAllByFavoriteOrViewedAndUser(favorite, viewed, user.getId());
            List<ItemDto> responses = new ArrayList<>();
            for (ItemViewedEntity itemViewed : listItemViewed) {
                ItemEntity item = itemRepository.findByIdAndDeleted(itemViewed.getItemId(), 0).orElse(null);
                if(item == null) continue;
                ItemDto itemDto = new ItemDto();
                BeanUtils.copyProperties(item, itemDto);
                responses.add(itemDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T extends ItemEntity> T saveNewItem(ItemDto itemDto, CategoryEntity category, BrandEntity brand, T item) {
//        JSONObject response = new JSONObject();
        try {
            BeanUtils.copyProperties(itemDto, item);
            item.setCategory(category);
            item.setBrand(brand);
            item.setDeleted(0);
            item = itemRepository.save(item);
            return item;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T extends ItemEntity> T updateItem(ItemDto itemDto, CategoryEntity category, BrandEntity brand, T item) {
        try {
            BeanUtils.copyProperties(itemDto, item);
            if (!category.getId().equals(item.getCategory().getId())) {
                item.setCategory(category);
            }
            if(!brand.getId().equals(item.getBrand().getId())) {
                item.setBrand(brand);
            }
            item = itemRepository.save(item);
            for(ImageDto image : itemDto.getImages()) {
                ImageEntity imageEntity = imageRepository.findById(image.getId()).orElse(null);
                BeanUtils.copyProperties(image, imageEntity);
                imageRepository.save(imageEntity);
            }
            return item;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
