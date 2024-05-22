package com.javatechie.repository;

import com.javatechie.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Integer> {
    List<CartItemEntity> findAllByCart_IdAndOrdered(Integer cartId, Integer ordered);
    Optional<CartItemEntity> findByItem_IdAndOrderedAndCart_Id(Integer itemId, Integer ordered, Integer cartId);
}
