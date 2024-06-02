package com.javatechie.controller;

import com.javatechie.dto.SupplierDto;
import com.javatechie.service.ISupplierService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SupplierController {
    @Autowired
    private ISupplierService supplierService;
    @PostMapping("/supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveSupplier(@RequestBody SupplierDto supplierDto) {
        JSONObject response = supplierService.saveSupplier(supplierDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateSupplier(@RequestBody SupplierDto supplierDto) {
        JSONObject response = supplierService.updateSupplier(supplierDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findOne(@RequestParam("id") Long id) {
        SupplierDto supplier = supplierService.findIOneSupplier(id);
        return ResponseEntity.ok(supplier);
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('ADMIN')")
    public ResponseEntity<?> findAll() {
        List<SupplierDto> suppliers = supplierService.findAllSupplier();
        return ResponseEntity.ok(suppliers);
    }

    @DeleteMapping("/supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteSupplier(@RequestParam("id") Long id) {
        JSONObject response = supplierService.deleteSupplier(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
