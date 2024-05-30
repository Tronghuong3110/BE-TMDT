package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.ImageDto;
import com.javatechie.dto.ProductDto;
import com.javatechie.dto.ProductItemDto;
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

import java.awt.*;
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
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private ProductRepository productRepository;

    // Thêm sản phẩm vào giỏ hàng
    @Override
    public JSONObject addItemToCart(CartItemDto cartItemDto) {
        JSONObject response = new JSONObject();
        try {
            CartItemEntity cartItem = new CartItemEntity();
            ProductItemEntity productItem = productItemRepository.findById(cartItemDto.getProductItemId()).orElse(null);
//            ItemDetailEntity itemDetail = itemDetailRepository.findByIdAndIsAvailableAndDeleted(cartItemDto.getItemDetail().getId(), true, 0).orElse(null);
            if(productItem == null) {
                response.put("code", 0);
                response.put("message", "Sản phẩm không tồn tại !!");
                return response;
            }
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
            if(user == null) {
                response.put("code", 0);
                response.put("message", "Bạn chưa đăng nhập !!");
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
            cartItem.setProductItem(productItem);
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setCart(cart);
            cartItem = cartItemRepository.save(cartItem);
            BeanUtils.copyProperties(cartItem, cartItemDto);
            response.put("code", 1);
            response.put("message", "Thêm vào giỏ hàng thành công !!");
            response.put("cartItem", cartItemDto);
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
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            CartEntity cart = cartRepository.findAllByUserId(user.getId()).orElse(null);
            // TH không tìm thấy user và giỏ hàng cuủa user
            if(user.getId() == null || cart == null) {
                return null;
            }
            List<CartItemEntity> listCartItem = cartItemRepository.findAllByCart_IdAndOrdered(cart.getId(), 0);
            List<CartItemDto> listResponse = new ArrayList<>();
            for(CartItemEntity cartItem : listCartItem) {
                CartItemDto cartItemDto = new CartItemDto();
                BeanUtils.copyProperties(cartItem, cartItemDto);
                Double price = Math.round(cartItem.getProductItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
                cartItemDto.setPrice(price);

                ProductEntity product = productRepository.findById(cartItem.getProductItem().getProduct().getId()).orElse(new ProductEntity());
                // Lấy ra danh sách ảnh
                List<ImageEntity> images = product.getImages();
                List<ImageDto> imageDtos = new ArrayList<>();
                ModelMapper mapper = MapperUtil.configModelMapper();
                for(ImageEntity image : images) {
                    ImageDto imageDto = new ImageDto();
                    mapper.map(image, imageDto);
                    imageDtos.add(imageDto);
                }
                cartItemDto.setProductItemId(product.getId());
                cartItemDto.setImages(imageDtos);

                ProductItemDto productItemDto = new ProductItemDto();
                BeanUtils.copyProperties(cartItem.getProductItem(), productItemDto);

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
