package com.javatechie.controller;

import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.service.IITemDetailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/api/item/detail")
@RestController
@CrossOrigin("*")
public class ItemDetailController {

    @Autowired
    private IITemDetailService iTemDetailService;

    @PostMapping("")
    public ResponseEntity<?> saveItemDetail(@RequestBody ItemDto itemDto) {
        JSONObject response = iTemDetailService.saveItemDetail(itemDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<?> findById(@RequestParam("id") Integer id) {
        ItemDetailDto response = iTemDetailService.findById(id);
        if(response == null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody ItemDetailDto itemDetailDto) {
        JSONObject response = iTemDetailService.updateItemDetail(itemDetailDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id) {
        JSONObject response = iTemDetailService.deleteItemDetail(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}
