package com.javatechie.controller;

import com.javatechie.dto.PaymentDto;
import com.javatechie.service.IPaymentService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PaymentController {
    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/customer/api/payments")
    public List<PaymentDto> findAllPayment() {
        return paymentService.findAllPayment();
    }

    @PostMapping("admin/api/payment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> savePayment(PaymentDto paymentDto) {
        JSONObject response = paymentService.savePayment(paymentDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
