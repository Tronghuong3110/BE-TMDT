package com.javatechie.controller;

import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.OrderDto;
import com.javatechie.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderDto> findAll() {
        return orderService.findAllOrder();
    }

    @GetMapping("/order")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CartItemDto> findAllItemOfOrder(@RequestParam("orderId")Integer orderId) {
        return orderService.getListItemOfOrder(orderId);
    }
}
