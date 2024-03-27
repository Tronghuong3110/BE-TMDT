package com.javatechie.service;

import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.OrderDto;

import java.util.List;

public interface IOrderService {

    List<OrderDto> findAllOrder();
    List<CartItemDto> getListItemOfOrder(Integer orderId);
}
