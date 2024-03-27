package com.javatechie.controller;

import com.javatechie.dto.PromotionDto;
import com.javatechie.service.IPromotionService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class PromotionController {

    @Autowired
    private IPromotionService promotionService;

    @GetMapping("/promotions")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PromotionDto> findAll() {
        return promotionService.findAll();
    }

    @GetMapping("/promotion")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findOne(@RequestParam("id")Integer id) {
        JSONObject response = promotionService.findOne(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promotion")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> savePromotion(@RequestBody PromotionDto promotionDto) {
        JSONObject response = promotionService.savePromotion(promotionDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/promotion")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePromotion(@RequestBody PromotionDto promotionDto) {
        JSONObject response = promotionService.update(promotionDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/promotion")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam("id") Integer id) {
        JSONObject response = promotionService.delete(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
