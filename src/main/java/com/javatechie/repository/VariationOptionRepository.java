package com.javatechie.repository;

import com.javatechie.entity.VariationOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariationOptionRepository extends JpaRepository<VariationOptionEntity, Long> {
}
