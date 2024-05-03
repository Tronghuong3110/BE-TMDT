package com.javatechie.repository;

import com.javatechie.entity.UserVoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucherEntity, Long> {
}
