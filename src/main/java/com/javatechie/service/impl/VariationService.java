package com.javatechie.service.impl;

import com.javatechie.dto.CategoryDto;
import com.javatechie.dto.VariationDto;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IVariationService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VariationService implements IVariationService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VariationRepository variationRepository;
    @Autowired
    private VariationOptionRepository variationOptionRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public JSONObject saveVariation(VariationDto variationDto, int categoryId) {
        JSONObject response = new JSONObject();
        try {
            CategoryEntity category = categoryRepository.findById(categoryId).orElse(new CategoryEntity());
            VariationEntity variation = new VariationEntity();
            BeanUtils.copyProperties(variationDto, variation);
            variation.setCategory(category);
            variation.setId(System.currentTimeMillis());
            variation = variationRepository.save(variation);
            BeanUtils.copyProperties(variation, variationDto);
            response.put("code", 1);
            response.put("message", "Thêm mới thông tin thành công !!");
            response.put("variation", variationDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới thông tin thất bại !!");
            response.put("variation", new VariationEntity());
        }
        return response;
    }

    @Override
    public JSONObject updateVariation(VariationDto variationDto, int categoryId) {
        JSONObject response = new JSONObject();
        try {
            ModelMapper mapper = MapperUtil.configModelMapper();
            VariationEntity variation = variationRepository.findById(variationDto.getId()).orElse(new VariationEntity());
            mapper.map(variationDto, variation);
            CategoryEntity category = categoryRepository.findById(categoryId).orElse(null);
            if(category != null) {
                variation.setCategory(category);
            }
            variation = variationRepository.save(variation);
            BeanUtils.copyProperties(variation, variationDto);
            response.put("code", 1);
            response.put("message", "Cập nhật thông tin thành công !!");
            response.put("variation", variationDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thông tin thất bại !!");
            response.put("variation", new VariationDto());
        }
        return response;
    }

    @Override
    public List<VariationDto> findAllByCategory(int categoryId) {
        try {
            List<VariationEntity> variations = variationRepository.findAllByCategory_Id(categoryId);
            List<VariationDto> responses = new ArrayList<>();
            for(VariationEntity variation : variations) {
                VariationDto variationDto = new VariationDto();
                BeanUtils.copyProperties(variation, variationDto);
                responses.add(variationDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public VariationDto findOneVariation(Long id) {
        try {
            VariationEntity variation = variationRepository.findById(id).orElse(null);
            if(variation == null) {
                return null;
            }
            VariationDto variationDto = new VariationDto();
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(variation, variationDto);
            return variationDto;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public JSONObject deleteVariation(Long id) {
        JSONObject response = new JSONObject();
        try {
            variationOptionRepository.deleteAllByVariation_Id(id);
            variationRepository.deleteById(id);
            response.put("code", 1);
            response.put("message", "Xóa thông tin thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 1);
            response.put("message", "Xóa thông tin thất bại !!");
        }
        return response;
    }

    @Override
    public JSONObject saveVariationOption(CategoryDto categoryDto, Long productId) {
        JSONObject response = new JSONObject();
        try {
            // mỗi bộ list<Variation> đại diện cho 1 sản phẩm chi tiết của sản phẩm
            List<JSONObject> productItemDetails = new ArrayList<>();
            ProductEntity product = productRepository.findById(productId).orElse(new ProductEntity());
            // Tạo mới 1 sản phẩm chi tiết của 1 sản phẩm ==> set số lượng đã bán, số lượng trong kho, giá = 0
            ProductItemEntity productItem = new ProductItemEntity();
            productItem.setId(System.currentTimeMillis());
            productItem.setProduct(product);
            productItem.setPrice((double)0);
            productItem.setQuantitySold(0);
            productItem.setQuantityInStock(0);
            productItem.setDeleted(0);
            productItem = productItemRepository.save(productItem);

            List<ProductItemEntity> productItems = new ArrayList<>();
            productItems.add(productItem);
            for(VariationEntity variation : productItem.getProduct().getCategory().getVariations()) { // duyệt toàn bộ variation theo category của sản phầm
                long unixTime = System.currentTimeMillis();
                // kiểm tra xem variation đang xét có được chỉnh sửa hay không
                JSONObject productDetail = new JSONObject();
                List<VariationDto> variationDtos = categoryDto.getVariations().stream().filter(variationTmp -> variationTmp.getId().equals(variation.getId())).toList();
                VariationOptionEntity variationOptionTmp = new VariationOptionEntity();
                variationOptionTmp.setVariation(variation);
                variationOptionTmp.setProductItems(productItems);
                variationOptionTmp.setUnixTime(unixTime);
                variationOptionTmp.setId(System.currentTimeMillis());
                if(!variationDtos.isEmpty()) { // variation này không phải là thuộc tính chung
                    variationOptionTmp.setValue(variationDtos.get(0).getVariationOptionValue());
                }
                else { // variation đang xét là thuộc tính chung của sản phẩm đang thực hiện thay đổi
                    List<Long> productItemIds = productItemRepository.findAllByProduct_id(productId);
                    JSONObject variationOption = variationOptionRepository.findByProduct(variation.getId(), productItemIds);
                    if(variationOption == null) {
                        continue;
                    }
                    variationOptionTmp.setValue(variationOption.get("value").toString());
                    variationOptionTmp.setUnixTime(Long.parseLong(variationOption.get("unix_time").toString()));
                }
                variationOptionTmp = variationOptionRepository.save(variationOptionTmp);
                productItem = productItemRepository.save(productItem);
                productDetail.put("name", variation.getName());
                productDetail.put("value", variationOptionTmp.getValue());
                productDetail.put("important", variation.getImportant());
                productDetail.put("id_variation_option", variationOptionTmp.getId());
                productItemDetails.add(productDetail);
            }
            JSONObject infoDetail = new JSONObject();
            infoDetail.put("quantity_sold", productItem.getQuantitySold());
            infoDetail.put("quantity_stock", productItem.getQuantityInStock());
            infoDetail.put("price", productItem.getPrice());
            infoDetail.put("productItemId", productItem.getId());
            response.put("code", 1);
            response.put("message", "Thêm mới thông tin thành công !!");
            response.put("itemDetail", productItemDetails);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới thông tin thất bại !!");
            response.put("itemDetail", null);
        }
        return response;
    }

//    private VariationEntity toEntity(VariationEntity variation, VariationDto variationDto) {
//        if(variationDto.getName() != null) {
//            variation.setName(variationDto.getName());
//        }
//        return variation;
//    }
}
