package com.javatechie.repository;

import com.javatechie.entity.UserVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucherEntity, Long> {

    @Query(value = "select * from user_voucher where user_id = :userId", nativeQuery = true)
    List<UserVoucherEntity> findAllByUser_IdAndDateEnd(@Param("userId")Integer userId);
}
