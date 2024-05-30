package com.javatechie.repository;

import com.javatechie.entity.UserVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucherEntity, Long> {

    @Query(value = "select * from user_voucher where user_id = :userId and used = :used", nativeQuery = true)
    List<UserVoucherEntity> findAllByUser_IdAndDateEndAndUsed(@Param("userId")Integer userId, @Param("used")Boolean used);
    Optional<UserVoucherEntity> findByUser_IdAndVoucher_Id(Integer userId, Integer voucherId);
}
