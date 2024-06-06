package com.javatechie.service;

import com.javatechie.dto.CartItemDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface ICartService {

    JSONObject addItemToCart(CartItemDto cartItemDto);
    List<CartItemDto> findAllItemInCartByUser();
    JSONObject updateCart(Integer cartItemId, Integer quantity);
    JSONObject deleteItemInCart(Integer cartItem);
}
