package com.javatechie.repository;


import com.javatechie.entity.LapTopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LapTopRepository extends JpaRepository<LapTopEntity, Integer> {
}
