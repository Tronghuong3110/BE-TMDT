package com.javatechie.controller;

import com.javatechie.dto.CategoryDto;
import com.javatechie.service.ICategoryService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/categories") // ok
    public List<CategoryDto> findAllCategory() {
        List<CategoryDto> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping("/admin/category") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findOneById(@RequestParam("id") Integer id) {
        JSONObject response = categoryService.findOneById(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/category") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
        JSONObject response = categoryService.saveCategory(categoryDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/category") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto categoryDto) {
        JSONObject response = categoryService.updateCategory(categoryDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/category") // ok
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCategory(@RequestParam("id") Integer id) {
        JSONObject response = categoryService.deleteCategory(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
