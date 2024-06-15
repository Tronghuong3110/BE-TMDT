package com.javatechie.controller;

import com.javatechie.service.impl.ItemService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController
@CrossOrigin("*")
public class ItemfavoriteController {
    @Autowired
    private ItemService itemService;

    @PostMapping("/item/favorite")
    public ResponseEntity<?> saveItemFavorite(@RequestParam("itemId")Long itemId) {
        JSONObject response = itemService.saveItemFavorite(itemId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/item/favorite/view")
    public ResponseEntity<?> findAllItem(@RequestParam("favorite") Optional<Integer> favorite, @RequestParam("viewed")Optional<Integer> viewed) {
        List<JSONObject> responses = itemService.findAllItemFavoriteOrViewed(favorite.orElse(null), viewed.orElse(null));
        if(responses == null) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        return ResponseEntity.ok(responses);
    }
}
