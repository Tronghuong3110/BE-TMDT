package com.javatechie.repository;

import com.javatechie.entity.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<ShipmentEntity, Long> {
    Optional<ShipmentEntity> findByName(String name);
}
