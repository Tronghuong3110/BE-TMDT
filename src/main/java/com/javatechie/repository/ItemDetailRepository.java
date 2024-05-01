package com.javatechie.repository;

import com.javatechie.entity.ItemDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDetailRepository extends JpaRepository<ItemDetailEntity, Integer> {
    Optional<ItemDetailEntity> findByIdAndIsAvailableAndDeleted(Integer id, Boolean isAvailable, Integer deleted);
    List<ItemDetailEntity> findAllByItem_IdAndDeleted(Integer itemId, Integer deleted);
    Optional<ItemDetailEntity> findByIdAndDeleted(Integer id, Integer deleted);
}
