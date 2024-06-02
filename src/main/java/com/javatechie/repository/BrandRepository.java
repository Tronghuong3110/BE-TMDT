package com.javatechie.repository;

import com.javatechie.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
    List<BrandEntity> findAllByDeleted(Integer deleted);
    Optional<BrandEntity> findByIdAndDeleted(Integer id, Integer deleted);
}
