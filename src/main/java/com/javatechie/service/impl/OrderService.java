package com.javatechie.service.impl;

import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.dto.OrderDto;
import com.javatechie.entity.*;
import com.javatechie.repository.CartItemRepository;
import com.javatechie.repository.CartRepository;
import com.javatechie.repository.OrderRepository;
import com.javatechie.service.IOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    // lấy ra danh sách đơn hàng đã đặt (dành cho admin)
    @Override
    public List<OrderDto> findAllOrder() {
        try {
            List<OrderEntity> listOrder = orderRepository.findAll();
            List<OrderDto> listResponse = new ArrayList<>();
            for(OrderEntity order : listOrder) {
                OrderDto orderDto = new OrderDto();
                BeanUtils.copyProperties(order, orderDto);
                String nameUser = order.getUser().getName();
                orderDto.setNameUser(nameUser);
                listResponse.add(orderDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // lấy ra danh sách các sản phẩm của 1 đơn hàng
    @Override
    public List<CartItemDto> getListItemOfOrder(Integer orderId) {
        try {
            OrderEntity order = orderRepository.findById(orderId).orElse(null);
            CartEntity cartEntity = order.getCart();
            List<CartItemEntity> listCartItem = cartItemRepository.findAllByCartId(cartEntity.getId());
            List<CartItemDto> listResponse = new ArrayList<>();
            for(CartItemEntity cartItem : listCartItem) {
                CartItemDto cartItemDto = new CartItemDto();
                BeanUtils.copyProperties(cartItem, cartItemDto);
                ItemDetailEntity item = cartItem.getItem();
                ItemDto itemDto = new ItemDto();
                BeanUtils.copyProperties(item, itemDto);
                cartItemDto.setItemDto(itemDto);
                listResponse.add(cartItemDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
