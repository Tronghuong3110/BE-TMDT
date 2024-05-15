package com.javatechie.repository;

import com.javatechie.entity.VariationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariationRepository extends JpaRepository<VariationEntity, Long> {
    List<VariationEntity> findAllByCategory_Id(int categoryId);
}
