package com.javatechie.repository;

import com.javatechie.entity.VariationOptionEntity;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOptionEntity, Long> {
    List<VariationOptionEntity> findAllByVariation_Id(Long variationId);
    void deleteAllByVariation_Id(Long variationId);


    @Modifying
    @Query(value = "update variation_option, product_variation set value = :value \n" +
                    "where unix_time = :unixTime and product_variation.product_item_id in :productItemIds", nativeQuery = true)
    void updateValueByUnixTime(@Param("value") String value, @Param("unixTime") Long unixTime,
                               @Param("productItemIds") List<Long> productItemIds);

    @Query(value = "select unix_time, value from variation_option, product_variation where variation_id = :variationId  \n" +
                    "and variation_option.id = product_variation.variation_option_id\n" +
                    "and product_variation.product_item_id in :productItemIds limit 1;", nativeQuery = true)
    JSONObject findByProduct(@Param("variationId") Long variationId, @Param("productItemIds") List<Long> productItemIds);
}
