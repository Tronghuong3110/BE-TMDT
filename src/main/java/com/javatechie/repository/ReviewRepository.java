package com.javatechie.repository;

import com.javatechie.entity.ReviewEntity;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Boolean existsByItem_IdAndUser_Id(Integer itemId, Integer userId);

    @Query(value = "select avg(ranking) as rating from review where item_id = :itemId group by item_id", nativeQuery = true)
    JSONObject calculatorAvgRakingByItem(Integer itemId);
    List<ReviewEntity> findAllByItem_Id(Integer itemId);
}
