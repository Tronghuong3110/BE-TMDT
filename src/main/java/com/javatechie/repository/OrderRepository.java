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
    @Query(value = "select * from orders where (:userId is null or user_id = :userId and (status_order_int = :status or :status is null )) and status_order_int != -1", nativeQuery = true)
    List<OrderEntity> findAllByUserId(@Param("userId")Integer userId,
                                      @Param("status") Integer status);

    // Lấy danh sách đơn hàng lọc theo ngày tháng
    @Query(value = "select * from orders where create_date between :start and :end and status_order_int not in (-1, 4)", nativeQuery = true)
    List<OrderEntity> findAllByCreateDateBetweenAndStatus(Date start, Date end);

    // lấy ra danh sách đơn haàng theo trạng thái
    @Query(value = "select * from orders where (:status is null or status_order_int = :status) and status_order_int != -1", nativeQuery = true)
    List<OrderEntity> findAllOrder(@Param("status") Integer status);

    // lấy ra danh sách các sản phẩm người dùng đã mua
    @Query(value = "select distinct product_item.product_id from orders " +
                    "join cart_item on cart_item.order_id = orders.id " +
                    "join product_item on cart_item.product_item_id = product_item.id " +
                    "where orders.user_id = :userId and orders.status_order_int not in (-1, 4);", nativeQuery = true)
    List<Long> findALlProductBoughtByUser(@Param("userId") Integer userId);
}
