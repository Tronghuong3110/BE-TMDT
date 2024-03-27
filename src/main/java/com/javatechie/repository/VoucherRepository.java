package com.javatechie.repository;

import com.javatechie.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Integer> {
    Boolean existsByName(String name);
    List<VoucherEntity> findAllByDeleted(Integer id);
    Optional<VoucherEntity> findByIdAndDeleted(Integer id, Integer deleted);
    Optional<VoucherEntity> findByNameAndDeleted(String name, Integer deleted);
}
