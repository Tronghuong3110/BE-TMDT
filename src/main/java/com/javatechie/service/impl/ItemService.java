package com.javatechie.service.impl;

import com.javatechie.dto.ImageDto;
import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IItemService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.awt.*;
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
                PhoneEntity phone = (PhoneEntity) saveNewItem(item, category, brand, new PhoneEntity());
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
                for(ItemDetailDto itemDetail : item.getItemDetails()) {
                    ItemDetailEntity itemDetailEntity = new ItemDetailEntity();
                    BeanUtils.copyProperties(itemDetail, itemDetailEntity);
                    itemDetailEntity.setItem(phone);
                    itemDetailRepository.save(itemDetailEntity);
                }
                response.put("message", "Add new phone success");
            }
            else if (category.getCode().equals("lap-top")) {
                LapTopEntity lapTop = (LapTopEntity) saveNewItem(item, category, brand, new LapTopEntity());
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
                for(ItemDetailDto itemDetail : item.getItemDetails()) {
                    ItemDetailEntity itemDetailEntity = new ItemDetailEntity();
                    BeanUtils.copyProperties(itemDetail, itemDetailEntity);
                    itemDetailEntity.setItem(lapTop);
                    itemDetailRepository.save(itemDetailEntity);
                }
                response.put("message", "Add new laptop success");
            }
            else if (category.getCode().equals("phu-kien")) {
                AccessoryEntity accessory = (AccessoryEntity) saveNewItem(item, category, brand, new AccessoryEntity());
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
                for(ItemDetailDto itemDetail : item.getItemDetails()) {
                    ItemDetailEntity itemDetailEntity = new ItemDetailEntity();
                    BeanUtils.copyProperties(itemDetail, itemDetailEntity);
                    itemDetailEntity.setItem(accessory);
                    itemDetailRepository.save(itemDetailEntity);
                }
                response.put("message", "Add new accessory success");
            }
            response.put("code", 1);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add new item error");
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
            ItemEntity itemEntity = itemRepository.findByIdAndDeleted(item.getId(), 0).orElse(null);
            if(itemEntity == null) {
                response.put("code", 0);
                response.put("message", "Can not found item with id = " + item.getId());
                return response;
            }
            itemEntity = updateItem(item, category, brand, itemEntity);
            response.put("code", 1);
            response.put("message", "Update item success");
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
            List<ItemDetailEntity> listItemDetail = itemDetailRepository.findAllByItem_Id(id);
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
            return itemDto;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            for(ItemDetailDto itemDetail : itemDto.getItemDetails()) {
                ItemDetailEntity itemDetailEntity = itemDetailRepository.findById(itemDetail.getId()).orElse(null);
                BeanUtils.copyProperties(itemDetail, itemDetailEntity);
                itemDetailRepository.save(itemDetailEntity);
            }
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
