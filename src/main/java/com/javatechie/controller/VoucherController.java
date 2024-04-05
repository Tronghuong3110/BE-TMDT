package com.javatechie.controller;

import com.javatechie.dto.VoucherDto;
import com.javatechie.service.IVoucherService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class VoucherController {
    @Autowired
    private IVoucherService voucherService;

    @GetMapping("/vouchers")
    public List<VoucherDto> findAll() {
        return voucherService.findAll();
    }

    @GetMapping("/admin/voucher")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> findOne(@RequestParam("id") Integer id) {
        JSONObject response = voucherService.findOne(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/voucher")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveVoucher(@RequestBody VoucherDto voucherDto) {
        JSONObject response = voucherService.saveVoucher(voucherDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/voucher")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateVoucher(@RequestBody VoucherDto voucherDto) {
        JSONObject response = voucherService.updateVoucher(voucherDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/voucher")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteVoucher(@RequestParam("id") Integer id) {
        JSONObject response = voucherService.deleteVoucher(id);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
