package com.javatechie.repository;

import com.javatechie.entity.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
    List<PromotionEntity> findAllByDeleted(Integer deleted);
    Optional<PromotionEntity> findByIdAndDeleted(Integer id, Integer deleted);
}
