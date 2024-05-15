package com.javatechie.repository;

import com.javatechie.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
//    List<ImageEntity> findAllByItemEntity(ItemEntity itemId);
}
