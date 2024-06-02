package com.javatechie.repository;

import com.javatechie.entity.BrandEntity;
import com.javatechie.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Boolean existsByName(String name);
    Optional<CategoryEntity> findByIdAndDeleted(Integer id, Integer deleted);
}
