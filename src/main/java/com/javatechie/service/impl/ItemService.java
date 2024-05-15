package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.*;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IItemService;
import jdk.jfr.Category;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService implements IItemService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ItemViewedRepository itemViewedRepository;
    @Autowired
    private VariationRepository variationRepository;;
    @Autowired
    private VariationOptionRepository variationOptionRepository;
    @Autowired
    private ProductItemRepository productItemRepository;

    @Override
    public JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto) {
        try {
            CategoryEntity category = categoryRepository.findById(categoryDto.getId()).orElse(new CategoryEntity());
            ProductEntity product = new ProductEntity();
            product.setId(System.currentTimeMillis());
            product.setName(productDto.getName());
            product.setCategory(category);
            product = productRepository.save(product);

            // lưu ảnh của product
            List<ImageEntity> images = new ArrayList<>();
            for(ImageDto imageDto : productDto.getImages()) {
                ImageEntity image = new ImageEntity();
                image.setPath(imageDto.getPath());
                images.add(image);
            }
            imageRepository.saveAll(images);

            // lưu product Item
            ProductItemEntity productItem = new ProductItemEntity();
            productItem.setId(System.currentTimeMillis());
            productItem.setSku(productDto.getProductItems().get(0).getSku());
            productItem.setProduct(product);
            productItem = productItemRepository.save(productItem);

            // lưu variation option
            for(VariationDto variationDto : categoryDto.getVariations()) {
                List<VariationOptionEntity> variationOptions = new ArrayList<>();
                VariationEntity variation = variationRepository.findById(variationDto.getId()).orElse(new VariationEntity());
                for(VariationOptionDto variationOption : variationDto.getVariationOptions()) {
                    VariationOptionEntity variationOptionTmp = new VariationOptionEntity();
                    variationOptionTmp.setId(System.currentTimeMillis());
                    variationOptionTmp.setValue(variationOption.getValue());
                    variationOptionTmp.setVariation(variation);
                    variationOptions.add(variationOptionTmp);
                }
                variationOptions = variationOptionRepository.saveAll(variationOptions);
                productItem.setVariationOptions(variationOptions);
                productItem = productItemRepository.save(productItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject updateProduct(ProductDto productDto, VariationDto variationDto, int categoryId) {
        return null;
    }
}
