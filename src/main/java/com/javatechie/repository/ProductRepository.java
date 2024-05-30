package com.javatechie.repository;

import com.javatechie.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Query(value = "select * from product where " +
                    "(:categoryId is null or category_id = :categoryId) " +
                    "and (:brandId is null or brand_id = :brandId) " +
                    "and (:key is null or name like %:key%) and deleted = 0", nativeQuery = true)
    List<ProductEntity> findAllProduct(@Param("categoryId") Integer categoryId,
                                       @Param("brandId") Integer brandId,
                                       @Param("key") String key);
}
