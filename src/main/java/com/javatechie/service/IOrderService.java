package com.javatechie.service;

import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.OrderDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IOrderService {

    List<OrderDto> findAllOrder();
    JSONObject getListItemOfOrder(Long orderId);
    JSONObject saveOrder(OrderDto orderDto, Long shipmentMethod, Long paymentMethod, Long voucherId, String orderInfo, String responseCode, String transactionCode, String bankTranNo);
    JSONObject paymentOnline(OrderDto order);
}
