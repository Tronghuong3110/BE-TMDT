package com.javatechie.controller;

import com.javatechie.dto.VariationDto;
import com.javatechie.service.IVariationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin/api")
public class VariationController {

    @Autowired
    private IVariationService variationService;

    @PostMapping("/variation")
    public ResponseEntity<?> saveVariation(@RequestBody VariationDto variationDto, @RequestParam("categoryId") Integer categoryId) {
        JSONObject response = variationService.saveVariation(variationDto, categoryId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/variations")
    public ResponseEntity<?> findAllVariationByCategory(@RequestParam("categoryId") Integer categoryId) {
        List<VariationDto> responses = variationService.findAllByCategory(categoryId);
        if(responses.size() <= 0) {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/variation")
    public ResponseEntity<?> updateVariation(@RequestBody VariationDto variationDto, @RequestParam("categoryId") Optional<Integer> categoryId) {
        JSONObject response = variationService.updateVariation(variationDto, categoryId.orElse(0));
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/variation")
    public ResponseEntity<?> deleteVariation(@RequestParam("variationId") Long variationId) {
        JSONObject response = variationService.deleteVariation(variationId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
