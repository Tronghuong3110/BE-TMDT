package com.javatechie.repository;

import com.javatechie.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
    Boolean existsByName(String name);
    List<VoucherEntity> findAllByDeleted(Integer id);
    @Query(value = "select * from voucher where (:id is null or id = :id) and deleted = 0 and :date <= end_date", nativeQuery = true)
    List<VoucherEntity> findById(@Param("id") Integer id,@Param("date") Date dateTime);
    Optional<VoucherEntity> findByNameAndDeleted(String name, Integer deleted);
}
