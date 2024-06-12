package com.javatechie.repository;

import com.javatechie.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "select * from orders where (:userId is null or user_id = :userId) ", nativeQuery = true)
    List<OrderEntity> findAllByUserId(@Param("userId")Integer userId);

    @Query(value = "select * from orders where create_date between :start and :end and status_order_int != 4", nativeQuery = true)
    List<OrderEntity> findAllByCreateDateBetweenAndStatus(Date start, Date end);
}
