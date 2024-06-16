package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ProductItemInvoiceRepository productItemInvoiceRepository;

    @Override
    public JSONObject saveProduct(ProductDto productDto, CategoryDto categoryDto, Integer brandId) {
        JSONObject response = new JSONObject();
        try {
            CategoryEntity category = categoryRepository.findById(categoryDto.getId()).orElse(new CategoryEntity());
            BrandEntity brand = brandRepository.findByIdAndDeleted(brandId, 0).orElse(new BrandEntity());
            ProductEntity product = new ProductEntity();
            product.setId(System.currentTimeMillis());
            product.setName(productDto.getName());
            product.setCategory(category);
            product.setDeleted(false);
            product.setBrand(brand);
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
            productItem.setPrice((double) 0);
            productItem.setQuantityInStock(0);
            productItem.setQuantitySold(0);
            productItem.setDeleted(0);
            productItem = productItemRepository.save(productItem);
            List<ProductItemEntity> productItems = new ArrayList<>();
            productItems.add(productItem);

            // lưu variation option
            for(VariationDto variationDto : categoryDto.getVariations()) {
                long unixTime = System.currentTimeMillis();
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
            response = findOneById(product.getId(), true);
            response.put("code", 1);
            response.put("message", "Thêm mới sản phẩm thành công!!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới sản phẩm thất bại !!");
            response.put("product", null);
        }
        return response;
    }

    @Transactional
    @Override
    public JSONObject updateProductDetail(Long productItemId, VariationDto variationDto) {
        JSONObject response = new JSONObject();
        try {
            // Lấy ra productItem ==> lấy ra danh sách variation option ==> sửa trường value
            // nếu thay đổi category hoặc brand th update lại
            // thay đổi ảnh ( có thể xóa ảnh cũ và thêm ảnh mới )
            ProductItemEntity productItem = productItemRepository.findById(productItemId).orElse(null);
            if(productItem == null) {
                response.put("code", 0);
                response.put("message", "Sản phẩm không tồn tại !!");
                return response;
            }
            // cập nhật value của variationOption
            List<VariationOptionEntity> variationOptions = productItem.getVariationOptions(); // lấy danh sách variationOption của 1 productItem
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(VariationOptionDto variationOptionDto : variationDto.getVariationOptions()) { // Lấy ra danh sách variationOption đang chỉnh sửa
//                List<VariationOptionEntity> variationOptionEntities = variationOptions.stream().filter(variationOptionEntity -> variationOptionEntity.getId().equals(variationOptionDto.getId())).toList();
//                if(variationOptionEntities.isEmpty()) continue;
                VariationOptionEntity variationOption = variationOptionRepository.findById(variationOptionDto.getId()).orElse(new VariationOptionEntity());
                mapper.map(variationOptionDto, variationOption);
                // cập nhật toàn bộ variationOption có của các productItem khác nhau mà thuộc cùng 1 product
//                if(variationOptionDto.getValue() != null) {
                List<Long> productItemIds = productItemRepository.findAllByProduct_id(productItem.getProduct().getId());
                variationOptionRepository.updateValueByUnixTime(variationOptionDto.getValue(), variationOption.getUnixTime(), productItemIds);
//                }
//                variationOptionRepository.save(variationOption);
            }
            response.put("code", 1);
            response.put("message", "Chỉnh sửa thông tin sản chi tiết sản phẩm thành công !!");
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

    @Override // test thành công
    public JSONObject findOneById(Long productId, boolean isFindAll) {
        JSONObject response = new JSONObject();
        try {
            ProductEntity product = productRepository.findByIdAndDeleted(productId, false).orElse(null);
            if(product == null) {
                response.put("message", "Sản phẩm không tồn tại");
                response.put("code", 0);
                return response;
            }
            // lấy thông tin sản phẩm
            response.put("productId", productId);
            response.put("name", product.getName());
            // Lấy danh sách ảnh
            List<ImageEntity> imageEntities = imageRepository.findAllByProduct_Id(productId);
            List<ImageDto> images = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(ImageEntity image : imageEntities) {
                ImageDto imageDto = new ImageDto();
                mapper.map(image, imageDto);
                images.add(imageDto);
            }
            response.put("images", images);
            // Lấy danh sách review (dành cho lấy ra thông tin sản phẩm chi tiêt)
            if(!isFindAll) {
                // danh sách review
                List<ReviewEntity> reviewEntities = product.getReviews();
                List<ReviewDto> reviews = new ArrayList<>();
                for(ReviewEntity review : reviewEntities) {
                    ReviewDto reviewDto = new ReviewDto();
                    mapper.map(review, reviewDto);
                    reviews.add(reviewDto);
                }
                response.put("reviews", reviews);
                // danh sách comment
                List<CommentEntity> commentEntities = product.getComments();
                List<CommentDto> comments = new ArrayList<>();
                for(CommentEntity comment : commentEntities) {
                    CommentDto commentDto = new CommentDto();
                    mapper.map(comment, commentDto);
                    commentDto.setFullName(comment.getUser().getName());
                    commentDto.setAvatarPath(comment.getUser().getAvatarPath());
                    commentDto.setUser(null);
                    comments.add(commentDto);
                }
                response.put("comments", comments);
                // danh sách số lượng đánh giá theo từng sao
                List<JSONObject> numberStar = reviewRepository.countStarByProduct(productId);
                response.put("number_star", numberStar);
            }
            // Lấy danh sách itemDetail (Là danh sách productItem)
            JSONParser parser = new JSONParser();
            List<ProductItemEntity> productItems = productItemRepository.findAllByProduct_IdAndDeleted(productId, 0);
            List<JSONArray> itemDetails = new ArrayList<>();
            for(ProductItemEntity productItem : productItems) {
                JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(productItem.getId());
                if(object == null) continue;
                JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
                JSONObject quantity = new JSONObject();
                quantity.put("quantity_stock", productItem.getQuantityInStock());
                quantity.put("quantity_sold", productItem.getQuantitySold());
                quantity.put("productItemId", productItem.getId());
                quantity.put("price", productItem.getPrice());
                productDetail.add(quantity);
                itemDetails.add(productDetail);
            }
            response.put("itemDetails", itemDetails);
            // Lấy thông tin category
            CategoryEntity category = product.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(category, categoryDto);
            categoryDto.setVariations(null);
            // Lấy thông tin về brand
            BrandDto brandDto = new BrandDto();
            BrandEntity brand = product.getBrand();
            mapper.map(brand, brandDto);
            brandDto.setItems(null);
            // Tính trung bình
            JSONObject object = reviewRepository.calculatorAvgRakingByItem(product.getId());
            response.put("rating", object == null ? 0 : object.get("rating"));
            response.put("number_rating", object == null ? 0 : object.get("number_rating"));
            response.put("category", categoryDto);
            response.put("brand", brandDto);
            response.put("description", product.getDescription());
            response.put("code", 1);

            // thêm mới vào danh sách item đã xem của user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object authPrincipal = auth.getPrincipal();
            if(!(authPrincipal.equals("anonymousUser")) && !isFindAll) {
                // TH đã đăng nhập
                UserInfoUserDetails userDetails = (UserInfoUserDetails) authPrincipal;
                User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
                // nếu tìm thấy ==> người dùng này đã xem sản phẩm này ==> không tạo mới nữa
                // nguợc lại ==> tạo mới và lưu vào db
                ItemViewedEntity itemViewed = itemViewedRepository.findByItemIdAndUserId(product.getId(), user.getId()).orElse(
                        new ItemViewedEntity(System.currentTimeMillis(), product.getId(), user.getId(), 1, 0));
                itemViewed.setViewed(1);
                itemViewedRepository.save(itemViewed);
            }
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

    @Override
    public JSONObject deleteProduct(Long productId) {
        JSONObject response = new JSONObject();
        try {
            ProductEntity product = productRepository.findById(productId).orElse(new ProductEntity());
            product.setDeleted(true);
            productRepository.save(product);
            response.put("code", 1);
            response.put("message", "Xóa sản phầm thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa sản phẩm thất bại !!");
        }
        return response;
    }

    @Override
    public JSONObject updateProduct(ProductDto productDto, Integer brandId) {
        JSONObject response = new JSONObject();
        try {
            ModelMapper mapper = MapperUtil.configModelMapper();
            ProductEntity product = productRepository.findById(productDto.getId()).orElse(new ProductEntity());
            mapper.map(productDto, product);
            // cập nhật thông tin về brand
            if(!product.getBrand().getId().equals(brandId)) {
                BrandEntity brand = brandRepository.findById(brandId).orElse(new BrandEntity());
                product.setBrand(brand);
            }
            product = productRepository.save(product);
            // cập nhật ảnh
            if(productDto.getImages() != null) {
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
            }
            mapper.map(product, productDto);
            productDto.setComments(null);
            productDto.setReviews(null);
            productDto.setProductItems(null);
            productDto.getCategory().setVariations(null);
            response.put("code", 1);
            response.put("message", "Cập nhật thông tin sản phẩm thành công !!");
            response.put("product", productDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thông tin sản phẩm thất bại !!");
            response.put("product", null);
        }
        return response;
    }

    @Override
    public JSONObject deleteItemDetail(Long productItemId) {
        JSONObject response = new JSONObject();
        try {
            ProductItemEntity productItem = productItemRepository.findById(productItemId).orElse(null);
            if(productItem != null) {
                productItem.setDeleted(1);
                productItemRepository.save(productItem);
            }
            response.put("code", 1);
            response.put("message", "Xóa sản phẩm chi tiết thành công !!");
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa sản phẩm chi tiết thất bại !!");
        }
        return response;
    }

    // Thêm mới sản phẩm yêu thích
    @Override
    public JSONObject saveItemFavorite(Long id) {
        JSONObject response = new JSONObject();
        try {
            ProductEntity item = productRepository.findByIdAndDeleted(id, false).orElse(new ProductEntity());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            ItemViewedEntity itemViewedEntity = itemViewedRepository.findByItemIdAndUserId(item.getId(), user.getId()).orElse(null);
            if(itemViewedEntity == null ) { // TH user chưa đánh dấu yêu thích sản phẩm này
                itemViewedEntity = new ItemViewedEntity(System.currentTimeMillis(), item.getId(), user.getId(), 0, 1);
                itemViewedEntity = itemViewedRepository.save(itemViewedEntity);
            }
            response.put("code", 1);
            response.put("message", "Thêm mới sản phẩm yêu thích thành công !!");
            response.put("itemFavorite", itemViewedEntity.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới sản phẩm yêu thích thất bại !!");
            response.put("itemFavorite", null);
        }
        return response;
    }

    // lấy ra danh sách sản phẩm yêu thích hoặc sản phẩm đã xem
    @Override
    public List<JSONObject> findAllItemFavoriteOrViewed(Integer favorite, Integer viewed) {
        try {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            List<ItemViewedEntity> listItemViewed = itemViewedRepository.findAllByFavoriteOrViewedAndUser(favorite, viewed, user.getId());
            List<JSONObject> responses = new ArrayList<>();
            for (ItemViewedEntity itemViewed : listItemViewed) {
                ProductEntity item = productRepository.findByIdAndDeleted(itemViewed.getItemId(), false).orElse(null);
                if(item == null) continue;
                JSONObject itemDto = findOneById(item.getId(), true);
                responses.add(itemDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override // nhập hàng
    public List<JSONObject> importItem(List<ProductItemDto> productItems, Long supplierId) {
        JSONArray responses = new JSONArray();
        try {
            SupplierEntity supplier = supplierRepository.findByIdAndDeleted(supplierId, 0).orElse(null);
            // tạo mới ra invoice và product_invoice
            InvoiceEntity invoice = new InvoiceEntity(System.currentTimeMillis(), new Date(System.currentTimeMillis()), null, supplier);
            invoice = invoiceRepository.save(invoice);
            // duyệt danh sách sản phẩm đang cần nhập
            for(ProductItemDto productItemDto : productItems) {
                JSONObject responseOneItem = new JSONObject();
                ProductItemEntity productItem = productItemRepository.findByIdAndDeleted(productItemDto.getId(), 0).orElse(null);
                if(productItem == null || supplier == null) {
                    responseOneItem.put("code", 0);
                    responseOneItem.put("message", "Không tồn tại sản phẩm có id: " + productItemDto.getId() + " hoặc không tồn tại nhà cung cấp có id: " + productItemDto.getSupplierId());
                    responses.add(responseOneItem);
                    continue;
                }
                // Cập nhật số lượng và giá bán của sản phẩm
                productItem.setQuantityInStock(productItem.getQuantityInStock() + productItemDto.getImportQuantity());
                productItem.setPrice(productItemDto.getImportPrice());
                productItem = productItemRepository.save(productItem);
                // tạo mới product_item_invoice
                ProductItemInvoiceEntity productItemInvoice = new ProductItemInvoiceEntity(System.currentTimeMillis(), productItemDto.getImportPrice(), productItemDto.getSoldPrice(), productItemDto.getImportQuantity(), new Date(System.currentTimeMillis()), invoice, productItem);
                productItemInvoice = productItemInvoiceRepository.save(productItemInvoice);
                responseOneItem.put("code", 1);
                responseOneItem.put("message", "Nhập sản phẩm có id: " + productItemDto.getId() + " thành công !!");
                responses.add(responseOneItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responses;
    }
}
