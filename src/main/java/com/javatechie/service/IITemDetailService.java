package com.javatechie.service;

import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import org.json.simple.JSONObject;

public interface IITemDetailService {
    JSONObject saveItemDetail(ItemDto itemDto);
    ItemDetailDto findById(Integer id);
    JSONObject updateItemDetail(ItemDetailDto itemDetailDto);

    JSONObject deleteItemDetail(Integer id);

}
