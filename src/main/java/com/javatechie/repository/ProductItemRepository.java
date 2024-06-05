package com.javatechie.repository;

import com.javatechie.entity.ProductItemEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItemEntity, Long> {
    @Query(value = "SELECT json_arrayagg(json_object('id_variation_option', variation_option.id, 'value', variation_option.value, 'name', variation.name, 'important', variation.important)) AS item_detail \n" +
                    "FROM variation_option " +
                    "JOIN product_variation ON variation_option.id = product_variation.variation_option_id " +
                    "JOIN variation ON variation.id = variation_option.variation_id " +
                    "JOIN product_item ON product_variation.product_item_id = product_item.id " +
                    "WHERE product_item.id = :productItemId and product_item.deleted = 0", nativeQuery = true)
    JSONObject findAllProductItemDetailByProductItem(@Param("productItemId") Long productItemId);

    @Query(value = "select product_item.* from product_item " +
                    "join product on product_item.product_id = product.id " +
                    "join category on category.id = product.category_id " +
                    "join brand on brand.id = product.brand_id " +
                    "where (:categoryId is null or category.id = :categoryId) " +
                    "and (:brandId is null or brand.id = :brandId) " +
                    "and (:key is null or product.name like %:key%) ", nativeQuery = true)
    List<ProductItemEntity> findAllByCategoryAndBrand(@Param("categoryId") Integer categoryId,
                                                      @Param("brandId") Integer brandId,
                                                      @Param("key") String key);

    List<ProductItemEntity> findAllByProduct_IdAndDeleted(Long productId, Integer deleted);

    @Query(value = "select id from product_item where product_id = :productId and deleted = 0", nativeQuery = true)
    List<Long> findAllByProduct_id(@Param("productId") Long productId);
    Optional<ProductItemEntity> findByIdAndDeleted(Long id, Integer deleted);
}
