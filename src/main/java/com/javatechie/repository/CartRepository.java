package com.javatechie.repository;

import com.javatechie.entity.CartEntity;
import com.javatechie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {

    @Query(value = "select * from cart where user_id = :userId order by unix_time limit 1", nativeQuery = true)
    Optional<CartEntity> findByUserAndOrdered(@Param("userId") Integer userId);

    Optional<CartEntity> findAllByUserId(Integer id);
}
