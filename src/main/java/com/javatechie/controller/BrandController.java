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

    @PostMapping("/admin/api/brand") // thêm mới hãng sản xuất
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveBrand(@RequestBody BrandDto brandDto) {
        JSONObject response = brandService.save(brandDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/api/brands") // lấy ra toàn bộ hãng sản xuất
    public List<BrandDto> findAll() {
        return brandService.findAllBrand();
    }

    @GetMapping("/admin/api/brand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findById(@RequestParam("brandId") Integer brandId) {
        JSONObject response = brandService.findOneById(brandId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/api/brand") // thêm mới hãng sản xuất
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateBrand(@RequestBody BrandDto brandDto) {
        JSONObject response = brandService.updateBrand(brandDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/api/brand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteBrand(@RequestParam("brandId") Integer brandId) {
        JSONObject response = brandService.deleteBrand(brandId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
