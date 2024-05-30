package com.javatechie.controller;

import com.javatechie.dto.ShipmentDto;
import com.javatechie.service.IShipmentService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")

public class ShipmentController {
    @Autowired
    private IShipmentService shipmentService;

    @GetMapping("/customer/api/shipments")
    public List<ShipmentDto> findAllPayment() {
        return shipmentService.findAllShipment();
    }

    @PostMapping("/admin/api/shipment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> savePayment(ShipmentDto shipmentDto) {
        JSONObject response = shipmentService.savePayment(shipmentDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
