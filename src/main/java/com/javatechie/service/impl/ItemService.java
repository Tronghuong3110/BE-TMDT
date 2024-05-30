package com.javatechie.service.impl;

import com.javatechie.dto.*;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IItemService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto) {
        JSONObject response = new JSONObject();
        try {
            CategoryEntity category = categoryRepository.findById(categoryDto.getId()).orElse(new CategoryEntity());
            ProductEntity product = new ProductEntity();
            product.setId(System.currentTimeMillis());
            product.setName(productDto.getName());
            product.setCategory(category);
            product.setDeleted(false);
            product = productRepository.save(product);

            // lưu ảnh của product
            List<ImageEntity> images = new ArrayList<>();
            for(ImageDto imageDto : productDto.getImages()) {
                ImageEntity image = new ImageEntity();
                image.setPath(imageDto.getPath());
                image.setProduct(product);
                images.add(image);
            }
            imageRepository.saveAll(images);

            // lưu product Item
            ProductItemEntity productItem = new ProductItemEntity();
            productItem.setId(System.currentTimeMillis());
            productItem.setProduct(product);
            productItem.setPrice((float) 0);
            productItem.setQuantityInStock(0);
            productItem.setQuantitySold(0);
            productItem = productItemRepository.save(productItem);
            List<ProductItemEntity> productItems = new ArrayList<>();
            productItems.add(productItem);

            // lưu variation option
            long unixTime = System.currentTimeMillis();
            for(VariationDto variationDto : categoryDto.getVariations()) {
                List<VariationOptionEntity> variationOptions = new ArrayList<>();
                VariationEntity variation = variationRepository.findById(variationDto.getId()).orElse(new VariationEntity());
                VariationOptionEntity variationOptionTmp = new VariationOptionEntity();
                variationOptionTmp.setId(System.currentTimeMillis());
                variationOptionTmp.setValue(variationDto.getVariationOptionValue());
                variationOptionTmp.setVariation(variation);
                variationOptionTmp.setProductItems(productItems);
                variationOptionTmp.setUnixTime(unixTime);
                variationOptionTmp = variationOptionRepository.save(variationOptionTmp);
                variationOptions.add(variationOptionTmp);
                productItem.setVariationOptions(variationOptions);
                productItem = productItemRepository.save(productItem);
            }
            response.put("code", 1);
            response.put("message", "Thêm mới sản phẩm thành công!!");
            productDto.setId(product.getId());
            productDto.setProductItemId(productItem.getId());
            response.put("product", productDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới sản phẩm thất bại !!");
            response.put("product", null);
        }
        return response;
    }

    @Override
    public JSONObject updateProduct(ProductDto productDto, VariationDto variationDto, Integer categoryId, Integer brandId) {
        JSONObject response = new JSONObject();
        try {
            // Lấy ra productItem ==> lấy ra danh sách variation option ==> sửa trường value
            // nếu thay đổi category hoặc brand th update lại
            // thay đổi ảnh ( có thể xóa ảnh cũ và thêm ảnh mới )
            ProductItemEntity productItem = productItemRepository.findById(productDto.getProductItemId()).orElse(null);
            if(productItem == null) {
                response.put("code", 0);
                response.put("message", "Sản phẩm không tồn tại !!");
                return response;
            }
            // cập nhật value của variationOption
            List<VariationOptionEntity> variationOptions = productItem.getVariationOptions();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(VariationOptionDto variationOptionDto : variationDto.getVariationOptions()) {
                VariationOptionEntity variationOption = variationOptions.stream().filter(variationOptionEnity -> variationOptionEnity.getId().equals(variationOptionDto.getId())).toList().get(0);
                mapper.map(variationOptionDto, variationOption);
                variationOptionRepository.save(variationOption);
            }
            // cập nhật thông tin về brand và category
            ProductEntity product = productItem.getProduct();
            if(!product.getCategory().getId().equals(categoryId)) {
                CategoryEntity category = categoryRepository.findById(categoryId).orElse(new CategoryEntity());
                product.setCategory(category);
            }
            if(!product.getBrand().getId().equals(brandId)) {
                BrandEntity brand = brandRepository.findById(brandId).orElse(new BrandEntity());
                product.setBrand(brand);
            }
            product = productRepository.save(product);
            // cập nhật ảnh
            List<Integer> idOlds = productDto.getImages()
                    .stream()
                    .filter(image -> image.getId() != null)
                    .map(ImageDto::getId)
                    .toList();
            List<ImageEntity> listImageOld = imageRepository.findAllByProduct_Id(product.getId());
            List<ImageDto> imageDtos = new ArrayList<>();
            // loại bỏ những hình ảnh đã xóa
            for(ImageEntity image : listImageOld) {
                if(!idOlds.contains(image.getId())) {
                    imageRepository.delete(image);
                }
            }
            // thêm mới image mới hoặc update lại path của image cũ
            for(ImageDto image : productDto.getImages()) {
                ImageDto imageDto = new ImageDto();
                ImageEntity imageEntity = null;
                if(image.getId() != null) {
                    imageEntity = imageRepository.findById(image.getId()).orElse(new ImageEntity());
                }
                else {
                    imageEntity = new ImageEntity();
                }
                mapper.map(image, imageEntity);
                imageEntity.setProduct(product);
                imageEntity = imageRepository.save(imageEntity);
                mapper.map(imageEntity, imageDto);
                imageDtos.add(imageDto);
            }
            mapper.map(product, productDto);
            productDto.setImages(imageDtos);
            productDto.setProductItems(null);
            productDto.getCategory().setVariations(null);
            response.put("code", 1);
            response.put("message", "Cập nhật thông tin sản phẩm thành công !!");
            response.put("item", productDto);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thông tin sản phẩm thất bại !!");
            response.put("item", null);
        }
        return response;
    }

    @Override
    public JSONObject findOneById(Long productId, boolean isFindAll) {
        JSONObject response = new JSONObject();
        try {
//            ProductItemEntity productItem = new ProductItemEntity();
            ProductEntity product = productRepository.findById(productId).orElse(null);
            if(product == null) {
                response.put("message", "Sản phẩm không tồn tại");
                response.put("code", 0);
                return response;
            }
            // lấy thông tin sản phẩm
            response.put("name", product.getName());
            // Lấy danh sách ảnh
            List<ImageEntity> imageEntities = product.getImages();
            List<ImageDto> images = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(ImageEntity image : imageEntities) {
                ImageDto imageDto = new ImageDto();
                mapper.map(image, imageDto);
                images.add(imageDto);
            }
            response.put("images", images);
            // Lấy danh sách review
            List<ReviewEntity> reviewEntities = product.getReviews();
            List<ReviewDto> reviews = new ArrayList<>();
            for(ReviewEntity review : reviewEntities) {
                ReviewDto reviewDto = new ReviewDto();
                mapper.map(review, reviewDto);
                reviews.add(reviewDto);
            }
            response.put("reviews", !isFindAll ? reviews : null);
            // Lấy danh sách comment
            List<CommentEntity> commentEntities = product.getComments();
            List<CommentDto> comments = new ArrayList<>();
            for(CommentEntity comment : commentEntities) {
                CommentDto commentDto = new CommentDto();
                mapper.map(comment, commentDto);
                comments.add(commentDto);
            }
            response.put("comments", !isFindAll ? comments : null);

            // Lấy danh sách itemDetail (Là danh sách productItem)
            JSONParser parser = new JSONParser();
            List<ProductItemEntity> productItems = productItemRepository.findAllByProduct_Id(productId);
            List<JSONObject> itemDetails = new ArrayList<>();
            for(ProductItemEntity productItem : productItems) {
                   JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(productItem.getId());
                   object.put("quantity_stock", productItem.getQuantityInStock());
                   object.put("quantity_sold", productItem.getQuantitySold());
                   itemDetails.add((JSONObject) parser.parse(object.get("item_detail").toString()));
            }
            response.put("itemDetails", itemDetails);

            // Lấy thông tin category
            CategoryEntity category = product.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(category, categoryDto);
            categoryDto.setVariations(null);
            // Tính trung bình
            JSONObject object = reviewRepository.calculatorAvgRakingByItem(product.getId());
            response.put("rating", object == null ? 0 : object.get("rating"));
            response.put("number_rating", object == null ? 0 : object.get("number_rating"));
            response.put("category", categoryDto);
            response.put("code", 1);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Sản phẩm không tồn tại");
            response.put("code", 0);
        }
        return response;
    }

    @Override
    public JSONArray findAll(Integer categoryId, Integer brandId, String key) {
        JSONArray array = new JSONArray();
        try {
            List<ProductEntity> products = productRepository.findAllProduct(categoryId, brandId, key);
            for(ProductEntity product : products) {
                JSONObject productEntity = findOneById(product.getId(), true);
                array.add(productEntity);
            }
            return array;
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
        return null;
    }
}
