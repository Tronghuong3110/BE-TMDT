package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.CartItemDto;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.ICartService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private CartRepository cartRepository;

    // Thêm sản phẩm vào giỏ hàng
    @Override
    public JSONObject addItemToCart(CartItemDto cartItemDto) {
        JSONObject response = new JSONObject();
        try {
            CartItemEntity cartItem = new CartItemEntity();
//            ItemDetailEntity itemDetail = itemDetailRepository.findByIdAndIsAvailableAndDeleted(cartItemDto.getItemDetail().getId(), true, 0).orElse(null);
//            if(itemDetail == null) {
                response.put("code", 0);
//                response.put("message", "Can not found itemDetail by id = " + cartItemDto.getItemDetail().getId());
//                return response;
//            }
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
            if(user == null) {
                response.put("code", 0);
                response.put("message", "User can not found");
                return response;
            }
            CartEntity cart = cartRepository.findByUserAndOrdered(user.getId()).orElse(null);
            // TH chưa tồn tại giỏ hàng nào hoặc giỏ hàng đã được thanh toán ==> tạo mới giỏ hàng
            if(cart == null) {
                cart = new CartEntity();
                cart.setUser(user);
                cart.setUnixTime(System.currentTimeMillis());
                cart = cartRepository.save(cart);
            }
//            cartItem.setItem(itemDetail);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setCart(cart);
            cartItem = cartItemRepository.save(cartItem);
            response.put("code", 1);
            response.put("message", "Add item to cart success");
//            response.put("cartItem", cartItem);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Add item to cart success");
        }
        return response;
    }

    @Override
    public List<CartItemDto> findAllItemInCartByUser() {
        try {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
            CartEntity cart = cartRepository.findAllByUserId(user.getId()).orElse(null);
            // TH không tìm thấy user và giỏ hàng cuủa user
            if(user == null || cart == null) {
                return null;
            }
            List<CartItemEntity> listCartItem = cartItemRepository.findAllByCart_IdAndOrdered(cart.getId(), 0);
            List<CartItemDto> listResponse = new ArrayList<>();
            for(CartItemEntity cartItem : listCartItem) {
                CartItemDto cartItemDto = new CartItemDto();
                BeanUtils.copyProperties(cartItem, cartItemDto);
//                Double price = Math.round(cartItem.getItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
//                cartItemDto.setPrice(price);

//                ItemDto itemDto = new ItemDto();
//                ItemEntity item = cartItem.getItem().getItem();
//                BeanUtils.copyProperties(item, itemDto);
//                cartItemDto.setItemDto(itemDto);
//
//                ItemDetailDto itemDetailDto = new ItemDetailDto();
//                BeanUtils.copyProperties(cartItem.getItem(), itemDetailDto);
//                cartItemDto.setItemDetail(itemDetailDto);

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