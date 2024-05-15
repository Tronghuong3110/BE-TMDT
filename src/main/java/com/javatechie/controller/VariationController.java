package com.javatechie.controller;

import com.javatechie.dto.VariationDto;
import com.javatechie.service.IVariationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class VariationController {

    @Autowired
    private IVariationService variationService;

    @PostMapping("/admin/api/variation")
    public ResponseEntity<?> saveVariation(@RequestBody VariationDto variationDto, @RequestParam("categoryId") Integer categoryId) {
        JSONObject response = variationService.saveVariation(variationDto, categoryId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/api/variations")
    public ResponseEntity<?> findAllVariationByCategory(@RequestParam("categoryId") Integer categoryId) {
        List<VariationDto> responses = variationService.findAllByCategory(categoryId);
        if(responses.size() <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(responses);
    }
}
