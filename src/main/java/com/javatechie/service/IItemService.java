package com.javatechie.service;

import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.entity.ItemEntity;
import org.json.simple.JSONObject;

import java.util.List;

public interface IItemService {
    JSONObject saveItem(ItemDto item, Integer categoryId, Integer brandId);
    JSONObject updateItem(ItemDto item, Integer categoryId, Integer brandId);
    JSONObject deleteItem(Integer id);
    ItemDto findOneById(Integer id);
    List<ItemDto> findAllItem(Integer categoryId, Integer brandId, String key);
    JSONObject saveItemFavorite(Integer id);
    List<ItemDto> findAllItemFavoriteOrViewed(Integer favorite, Integer viewed);
}
