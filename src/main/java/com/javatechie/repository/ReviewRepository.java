package com.javatechie.repository;

import com.javatechie.entity.ReviewEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Boolean existsByProduct_IdAndUser_Id(Long productId, Integer userId);

    @Query(value = "select avg(ranking) as rating, count(*) as number_rating from review where product_id = :productId group by product_id", nativeQuery = true)
    JSONObject calculatorAvgRakingByItem(@Param("productId") Long productId);

    @Query(value = "select ranking, count(*) as number_ranking from review " +
                    "where product_id = :productId " +
                    "group by ranking", nativeQuery = true)
    List<JSONObject> countStarByProduct(@Param("productId") Long productId);
}
