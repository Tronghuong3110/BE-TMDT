package com.javatechie.repository;

import com.javatechie.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Boolean existsByProduct_IdAndUser_Id(Integer itemId, Integer userId);
}
