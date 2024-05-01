package com.javatechie.controller;

import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.service.IItemService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class ItemController {
    @Autowired
    private IItemService itemService;

    @PostMapping("/admin/api/item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveItem(@RequestBody ItemDto itemDto, @RequestParam("categoryId")Integer categoryId, @RequestParam("brandId")Integer brandId) {
        JSONObject response = itemService.saveItem(itemDto, categoryId, brandId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/api/item") // lấy ra thông tin chi tiết 1 sản phẩm (chưa đăng nhập cũng có thể xem)
    public ResponseEntity<?> findOne(@RequestParam("id") Integer id) {
        ItemDto itemDto = itemService.findOneById(id);
        if(itemDto == null) {
            return ResponseEntity.status(500).body("Can not found");
        }
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/customer/api/items") // lấy ra tất cả sản phẩm (chưa đăng nhập cũng có thể xem)
    public ResponseEntity<?> findAllItem(@RequestParam("categoryId") Optional<Integer> categoryId, @RequestParam("brandId") Optional<Integer> brandId, @RequestParam("key")Optional<String> key) {
        List<ItemDto> listResponse = itemService.findAllItem(categoryId.orElse(null), brandId.orElse(null), key.orElse(null));
        if(listResponse == null) {
            return ResponseEntity.badRequest().body("Can not found item!!");
        }
        return ResponseEntity.ok(listResponse);
    }

    @PutMapping("/admin/api/item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateItem(@RequestBody ItemDto itemDto, @RequestParam("categoryId")Integer categoryId, @RequestParam("brandId")Integer brandId) {
        JSONObject response = itemService.updateItem(itemDto, categoryId, brandId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/api/item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteItem(@RequestParam("id")Integer id) {
        JSONObject response = itemService.deleteItem(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


}
