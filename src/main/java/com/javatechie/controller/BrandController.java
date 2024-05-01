package com.javatechie.controller;

import com.javatechie.dto.BrandDto;
import com.javatechie.service.IBrandService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @PostMapping("/admin/api/brand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveBrand(@RequestBody BrandDto brandDto) {
        JSONObject response = brandService.save(brandDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/brands")
    public List<BrandDto> findAll() {
        return brandService.findAllBrand();
    }
}
