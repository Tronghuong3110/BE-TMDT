package com.javatechie.repository;

import com.javatechie.entity.ReviewEntity;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Boolean existsByItem_IdAndUser_Id(Integer itemId, Integer userId);

    @Query(value = "select avg(ranking) as rating from btl_tmdt_old.review where item_id = :itemId group by item_id", nativeQuery = true)
    JSONObject calculatorAvgRakingByItem(Integer itemId);
}
