package com.javatechie.controller;

import com.javatechie.dto.InvoiceDto;
import com.javatechie.service.IInvoiceService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RequestMapping("/api")
@CrossOrigin("*")
@RestController
public class InvoiceController {
    @Autowired
    private IInvoiceService invoiceService;

    @PostMapping("/invoices")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public List<InvoiceDto> findAllInvoice(@RequestBody JSONObject request) {
//        String start = request.get("start").toString();
//        String end = request.get("end").toString();
        Date start = null;
        Date end = null;
        if(request.get("start") != "") {
            start = Date.valueOf(request.get("start").toString());
        }
        if(request.get("end") != "") {
            end = Date.valueOf(request.get("end").toString());
        }
        return invoiceService.findAllInvoice(start, end);
    }

    @GetMapping("/invoice")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('EMPLOYEE')")
    public InvoiceDto findOneInvoice(@RequestParam("invoiceId") Long invoiceId) {
        return invoiceService.findOneById(invoiceId);
    }
}
