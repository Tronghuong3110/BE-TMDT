package com.javatechie.service.impl;

import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.entity.ItemDetailEntity;
import com.javatechie.entity.ItemEntity;
import com.javatechie.repository.ItemDetailRepository;
import com.javatechie.repository.ItemRepository;
import com.javatechie.service.IITemDetailService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDetailService implements IITemDetailService {

    @Autowired
    private ItemDetailRepository itemDetailRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public JSONObject saveItemDetail(ItemDto itemDto) {
        JSONObject response = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            ItemEntity item = itemRepository.findByIdAndDeleted(itemDto.getId(), 0).orElse(null);
            if(item == null) {
                response.put("code", 0);
                response.put("message", "Can not save itemDetail because Item does not exists!!");
                response.put("array", jsonArray);
                return response;
            }
            for(ItemDetailDto itemDetailDto : itemDto.getItemDetails()) {
                ItemDetailEntity itemDetail = new ItemDetailEntity();
                BeanUtils.copyProperties(itemDetailDto, itemDetail);
                itemDetail.setItem(item);
                itemDetail.setDeleted(0);
                itemDetail = itemDetailRepository.save(itemDetail);
                BeanUtils.copyProperties(itemDetail, itemDetailDto);
                jsonArray.add(itemDetailDto);
            }
            response.put("code", 1);
            response.put("message", "Add new itemDetail success!!");
            response.put("array", jsonArray);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Add new itemDetail fail!!");
        }
        return response;
    }

    @Override
    public ItemDetailDto findById(Integer id) {
        try {
            ItemDetailDto itemDetailDto = new ItemDetailDto();
            ItemDetailEntity itemDetailEntity = itemDetailRepository.findByIdAndDeleted(id, 0).orElse(null);
            BeanUtils.copyProperties(itemDetailEntity, itemDetailDto);
            return itemDetailDto;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject updateItemDetail(ItemDetailDto itemDetailDto) {
        JSONObject response = new JSONObject();
        try {
            ItemDetailEntity itemDetailEntity = itemDetailRepository.findByIdAndDeleted(itemDetailDto.getId(), 0).orElse(null);
            BeanUtils.copyProperties(itemDetailDto, itemDetailEntity);
            itemDetailRepository.save(itemDetailEntity);
            response.put("code", 1);
            response.put("itemDetail", itemDetailDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("itemDetail", null);
        }
        return response;
    }

    @Override
    public JSONObject deleteItemDetail(Integer id) {
        JSONObject response = new JSONObject();
        try {
            ItemDetailEntity itemDetailEntity = itemDetailRepository.findByIdAndDeleted(id, 0).orElse(null);
            itemDetailEntity.setDeleted(1);
            itemDetailRepository.save(itemDetailEntity);
            response.put("code", 1);
            response.put("message", "Deleted itemDetail success");
        }
        catch(Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Deleted itemDetail fail");
        }
        return response;
    }
}
