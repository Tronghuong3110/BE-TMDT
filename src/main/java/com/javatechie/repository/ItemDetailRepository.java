package com.javatechie.repository;

import com.javatechie.entity.ItemDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDetailRepository extends JpaRepository<ItemDetailEntity, Integer> {
    Optional<ItemDetailEntity> findByIdAndIsAvailable(Integer id, Boolean isAvailable);
    List<ItemDetailEntity> findAllByItem_Id(Integer itemId);
}
