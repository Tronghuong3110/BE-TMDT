package com.javatechie.controller;

import com.javatechie.dto.CartItemDto;
import com.javatechie.service.ICartService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CartController {

    @Autowired
    private ICartService cartService;

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }

    @PostMapping("/customer/api/cart")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        JSONObject response = cartService.addItemToCart(cartItemDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("api/carts")
    public ResponseEntity<?> findAllCartItemInCart() {
        List<CartItemDto> listCartItem = cartService.findAllItemInCartByUser();
        if(listCartItem == null) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(listCartItem);
    }
}
