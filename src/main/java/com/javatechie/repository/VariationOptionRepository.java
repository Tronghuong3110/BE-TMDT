package com.javatechie.repository;

import com.javatechie.entity.VariationOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOptionEntity, Long> {
    List<VariationOptionEntity> findAllByVariation_Id(Long variationId);
    void deleteAllByVariation_Id(Long variationId);
}
