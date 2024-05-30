package com.javatechie.controller;

import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.ProductDto;
import com.javatechie.dto.RequestItemDto;
import com.javatechie.dto.RequestUpdate;
import com.javatechie.service.IItemService;
import com.javatechie.service.IVariationService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ItemController {
    @Autowired
    private IItemService itemService;
    @Autowired
    private IVariationService variationService;

    @PostMapping("/admin/api/item")
    @PreAuthorize("hasAuthority('ADMIN')") // ok
    public ResponseEntity<?> saveItem(@RequestBody RequestItemDto requestItemDto) {
        JSONObject response = itemService.saveProduct(requestItemDto.getProduct(), requestItemDto.getCategory());
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/api/item") // ok
    public ResponseEntity<?> findOne(@RequestParam("productId") Long productId) {
        JSONObject response = itemService.findOneById(productId, false);
        if(response.get("code").equals(0)) {
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/api/items") // ok
    public ResponseEntity<?> findAllItem(@RequestParam("categoryId") Optional<Integer> categoryId, @RequestParam("brandId") Optional<Integer> brandId, @RequestParam("key")Optional<String> key) {
        JSONArray listResponse = itemService.findAll(categoryId.orElse(null), brandId.orElse(null), key.orElse(null));
        if(listResponse == null) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        return ResponseEntity.ok(listResponse);
    }

    @PutMapping("/admin/api/item")
    @PreAuthorize("hasAuthority('ADMIN')") // ok
    public ResponseEntity<?> updateItem(@RequestBody RequestUpdate request, @RequestParam("categoryId")Integer categoryId, @RequestParam("brandId")Integer brandId) {
        JSONObject response = itemService.updateProduct(request.getProduct(), request.getVariation(), categoryId, brandId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/api/item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteItem(@RequestParam("id")Integer id) {
        JSONObject response = new JSONObject();
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/api/item/detail") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveItemDetail(@RequestBody CategoryDto categoryDto, @RequestParam("productId") Long productId) {
        JSONObject response = variationService.saveVariationOption(categoryDto, productId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
