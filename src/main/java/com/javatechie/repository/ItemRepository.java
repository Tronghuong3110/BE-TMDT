package com.javatechie.repository;

import com.javatechie.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    Optional<ItemEntity> findByIdAndDeleted(Integer id, Integer deleted);
    @Query(value = "select * from item where (:categoryId is null or category_id = :categoryId) and (:brandId is null or brand_id = :brandId) and (:key is null or name like %:key%) and deleted = 0", nativeQuery = true)
    List<ItemEntity> findAllByDeletedAndCategory_IdAndBrand_IdAndKey(@Param("categoryId") Integer categoryId,
                                                                     @Param("brandId") Integer brandId,
                                                                     @Param("key") String key);
}
