package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.ItemDetailDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.ICartService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService implements ICartService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ItemDetailRepository itemDetailRepository;
    @Autowired
    private CartRepository cartRepository;

    // Thêm sản phẩm vào giỏ hàng
    @Override
    public JSONObject addItemToCart(CartItemDto cartItemDto) {
        JSONObject response = new JSONObject();
        try {
            CartItemEntity cartItem;
            ItemDetailEntity itemDetail = itemDetailRepository.findByIdAndIsAvailableAndDeleted(cartItemDto.getItemDetail().getId(), true, 0).orElse(null);
            if(itemDetail == null) {
                response.put("code", 0);
                response.put("message", "Can not found itemDetail by id = " + cartItemDto.getItemDetail().getId());
                return response;
            }
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
            cartItem = cartItemRepository.findByItem_IdAndOrderedAndCart_Id(itemDetail.getId(), 0, cart.getId()).orElse(null);
            if(cartItem == null) {
                cartItem = new CartItemEntity();
                cartItem.setItem(itemDetail);
                cartItem.setQuantity(cartItemDto.getQuantity());
                cartItem.setCart(cart);
                cartItem.setOrdered(0);
            }
            else {
                cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity());
            }

            cartItem = cartItemRepository.save(cartItem);
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(cartItem, cartItemDto);
            // itemDetail
            ItemDetailDto itemDetailDto = new ItemDetailDto();
            mapper.map(itemDetail, itemDetailDto);
            // Item
            ItemEntity item = itemDetail.getItem();
            CategoryEntity category = item.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(category, categoryDto);
            ItemDto itemDto = new ItemDto();
            mapper.map(item, itemDto);
            itemDetailDto.setItemDto(itemDto);
            itemDto.setItemDetails(null);
            categoryDto.setItems(null);
            itemDto.setCategoryDto(categoryDto);
            cartItemDto.setItemDto(itemDto);

            cartItemDto.setPrice(itemDetail.getPrice() * cartItem.getQuantity());
            itemDetailDto.setItemDto(null);
            cartItemDto.setItemDetail(itemDetailDto);
            response.put("code", 1);
            response.put("item", cartItemDto);
            response.put("message", "Add item to cart success");
//            response.put("cartItem", cartItem);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Add item to cart fail");
            response.put("item", null);
        }
        return response;
    }

    @Override
    public List<CartItemDto> findAllItemInCartByUser() {
        try {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            CartEntity cart = cartRepository.findAllByUserId(user.getId()).orElse(new CartEntity());
            // TH không tìm thấy user và giỏ hàng cuủa user
            if(user.getId() == null || cart.getId() == null) {
                return null;
            }
            List<CartItemEntity> listCartItem = cartItemRepository.findAllByCart_IdAndOrdered(cart.getId(), 0);
            List<CartItemDto> listResponse = new ArrayList<>();
            for(CartItemEntity cartItem : listCartItem) {
                CartItemDto cartItemDto = new CartItemDto();
                ModelMapper mapper = MapperUtil.configModelMapper();
                mapper.map(cartItem, cartItemDto);
                Double price = Math.round(cartItem.getItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
                cartItemDto.setPrice(price);

                ItemDto itemDto = new ItemDto();
                ItemEntity item = cartItem.getItem().getItem();
                mapper.map(item, itemDto);
                itemDto.setItemDetails(null);
                CategoryEntity category = item.getCategory();
                CategoryDto categoryDto = new CategoryDto();
                mapper.map(category, categoryDto);
                categoryDto.setItems(null);
                itemDto.setCategoryDto(categoryDto);
                cartItemDto.setItemDto(itemDto);

                ItemDetailDto itemDetailDto = new ItemDetailDto();
                BeanUtils.copyProperties(cartItem.getItem(), itemDetailDto);
                cartItemDto.setItemDetail(itemDetailDto);

                listResponse.add(cartItemDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject updateCart(Integer cartItemId, Integer quantity) {
        JSONObject response = new JSONObject();
        try {
            CartItemEntity cartItem = cartItemRepository.findById(cartItemId).orElse(null);
            cartItem.setQuantity(quantity);
            cartItem = cartItemRepository.save(cartItem);
            CartItemDto cartItemDto = new CartItemDto();
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(cartItem, cartItemDto);
            Double price = Math.round(cartItem.getItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
            cartItemDto.setPrice(price);

            ItemDto itemDto = new ItemDto();
            ItemEntity item = cartItem.getItem().getItem();
            mapper.map(item, itemDto);
            itemDto.setItemDetails(null);
            itemDto.setReviews(null);
            CategoryEntity category = item.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(category, categoryDto);
            categoryDto.setItems(null);
            itemDto.setCategoryDto(categoryDto);
            cartItemDto.setItemDto(itemDto);

            ItemDetailDto itemDetailDto = new ItemDetailDto();
            BeanUtils.copyProperties(cartItem.getItem(), itemDetailDto);
            cartItemDto.setItemDetail(itemDetailDto);

            response.put("code", 1);
            response.put("message", "update item in cart success");
            response.put("cartItem", cartItemDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Update item in cart fail!");
            response.put("cartItem", null);
        }
        return response;
    }

    @Override
    public JSONObject deleteItemInCart(Integer cartItem) {
        JSONObject response = new JSONObject();
        try {
            cartItemRepository.deleteById(cartItem);
            response.put("code", 1);
            response.put("message", "delete item in cart success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Delete item in cart fail");
        }
        return response;
    }
}
