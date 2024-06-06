package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.*;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.ICartService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
    @Override // test thanh cong
    public JSONObject addItemToCart(CartItemDto cartItemDto) {
        JSONObject response = new JSONObject();
        try {
            CartItemEntity cartItem = new CartItemEntity();
            ProductItemEntity productItem = productItemRepository.findByIdAndDeleted(cartItemDto.getProductItemId(), 0).orElse(null);
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
            cartItem = cartItemRepository.findByProductItem_IdAndOrderedAndCart_Id(productItem.getId(), 0, cart.getId()).orElse(null);
            if(cartItem == null) {
                cartItem = new CartItemEntity();
                cartItem.setProductItem(productItem);
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
            // Set giá
            Double price = Math.round(cartItem.getProductItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
            cartItemDto.setTotalPrice(price);
            ProductEntity product = productRepository.findById(cartItem.getProductItem().getProduct().getId()).orElse(new ProductEntity());
            // Lấy ra danh sách ảnh
            List<ImageEntity> images = product.getImages();
            List<ImageDto> imageDtos = new ArrayList<>();
            for(ImageEntity image : images) {
                ImageDto imageDto = new ImageDto();
                mapper.map(image, imageDto);
                imageDtos.add(imageDto);
            }
            cartItemDto.setProductId(product.getId());
            cartItemDto.setImages(imageDtos);
            JSONParser parser = new JSONParser();
            JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(productItem.getId());
            JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
            JSONObject quantity = new JSONObject();
            quantity.put("quantity_stock", productItem.getQuantityInStock());
            quantity.put("quantity_sold", productItem.getQuantitySold());
            quantity.put("productItem", productItem.getId());
            productDetail.add(quantity);
            cartItemDto.setProductItemDetail(productDetail);
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

    @Override // test thành công
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
                cartItemDto.setTotalPrice(price);
                ProductEntity product = productRepository.findByIdAndDeleted(cartItem.getProductItem().getProduct().getId(), false).orElse(new ProductEntity());
                // Lấy ra danh sách ảnh
                List<ImageEntity> images = product.getImages();
                List<ImageDto> imageDtos = new ArrayList<>();
                ModelMapper mapper = MapperUtil.configModelMapper();
                for(ImageEntity image : images) {
                    ImageDto imageDto = new ImageDto();
                    mapper.map(image, imageDto);
                    imageDtos.add(imageDto);
                }
                cartItemDto.setProductId(product.getId());
                cartItemDto.setImages(imageDtos);
                JSONParser parser = new JSONParser();
                JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(cartItem.getProductItem().getId());
                JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
                JSONObject quantity = new JSONObject();
                quantity.put("quantity_stock", cartItem.getProductItem().getQuantityInStock());
                quantity.put("quantity_sold", cartItem.getProductItem().getQuantitySold());
                quantity.put("productItemId", cartItem.getProductItem().getId());
                quantity.put("price", cartItem.getProductItem().getPrice());
                productDetail.add(quantity);
                cartItemDto.setProductItemDetail(productDetail);
                cartItemDto.setProductName(product.getName());
                // Lấy ra category
                CategoryEntity category = product.getCategory();
                CategoryDto categoryDto = new CategoryDto();
                mapper.map(category, categoryDto);
                categoryDto.setVariations(null);
                cartItemDto.setCategory(categoryDto);
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
            CartItemEntity cartItem = cartItemRepository.findById(cartItemId).orElse(new CartItemEntity());
            ProductItemEntity productItem = cartItem.getProductItem();
            cartItem.setQuantity(quantity);
            cartItem = cartItemRepository.save(cartItem);
            CartItemDto cartItemDto = new CartItemDto();
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(cartItem, cartItemDto);
            Double price = Math.round(cartItem.getProductItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
            mapper.map(cartItem, cartItemDto);
            cartItemDto.setTotalPrice(price);
            ProductEntity product = productRepository.findById(cartItem.getProductItem().getProduct().getId()).orElse(new ProductEntity());
            // Lấy ra danh sách ảnh
            List<ImageEntity> images = product.getImages();
            List<ImageDto> imageDtos = new ArrayList<>();
            for(ImageEntity image : images) {
                ImageDto imageDto = new ImageDto();
                mapper.map(image, imageDto);
                imageDtos.add(imageDto);
            }
            cartItemDto.setProductId(product.getId());
            cartItemDto.setImages(imageDtos);
            JSONParser parser = new JSONParser();
            JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(cartItem.getProductItem().getId());
            JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
            JSONObject quantityObj = new JSONObject();
            quantityObj.put("quantity_stock", cartItem.getProductItem().getQuantityInStock());
            quantityObj.put("quantity_sold", cartItem.getProductItem().getQuantitySold());
            quantityObj.put("productItemId", cartItem.getProductItem().getId());
            quantityObj.put("price", cartItem.getProductItem().getPrice());
            productDetail.add(quantityObj);
            cartItemDto.setProductItemDetail(productDetail);
            cartItemDto.setProductName(product.getName());
            // Lấy ra category
            CategoryEntity category = product.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(category, categoryDto);
            categoryDto.setVariations(null);
            cartItemDto.setCategory(categoryDto);
            cartItemDto.setProductItemDetail(productDetail);

            response.put("code", 1);
            response.put("message", "Cập nhật thông tin giỏ hàng thành công !!");
            response.put("cartItem", cartItemDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thông tin giỏ hàng thất bại !!");
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
            response.put("message", "Xóa sản phẩm trong giỏ hàng thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa sản phẩm trong giỏ hàng thất bại !!");
        }
        return response;
    }
}
