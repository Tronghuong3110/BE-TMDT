package com.javatechie.repository;

import com.javatechie.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    @Query(value = "select * from orders where (:userId is null or user_id = :userId) ", nativeQuery = true)
    List<OrderEntity> findAllByUserId(@Param("userId")Integer userId);
}
