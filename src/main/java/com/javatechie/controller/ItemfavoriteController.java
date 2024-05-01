package com.javatechie.controller;

import com.javatechie.dto.ItemDto;
import com.javatechie.service.impl.ItemService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin("*")
public class ItemfavoriteController {
    @Autowired
    private ItemService itemService;

    @PostMapping("/item/favorite")
    public ResponseEntity<?> saveItemFavorite(@RequestParam("itemId")Integer itemId) {
        JSONObject response = itemService.saveItemFavorite(itemId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/item/favorite/view")
    public ResponseEntity<?> findAllItem(@RequestParam("favorite")Integer favorite, @RequestParam("viewed")Integer viewed) {
        List<ItemDto> responses = itemService.findAllItemFavoriteOrViewed(favorite, viewed);
        if(responses == null) {
            return ResponseEntity.badRequest().body(responses);
        }
        return ResponseEntity.ok(responses);
    }
}
