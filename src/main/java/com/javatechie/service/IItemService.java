package com.javatechie.service;

import com.javatechie.dto.ItemDto;
import org.json.simple.JSONObject;

public interface IItemService {
    JSONObject saveItem(ItemDto item, Integer categoryId, Integer brandId);
    JSONObject updateItem(ItemDto item, Integer categoryId, Integer brandId);
    JSONObject deleteItem(Integer id);
    ItemDto findOneById(Integer id);
}
