package com.javatechie.repository;

import com.javatechie.entity.ProductItemEntity;
import com.javatechie.entity.ProductItemInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ProductItemInvoiceRepository extends JpaRepository<ProductItemInvoiceEntity, Long> {
    List<ProductItemInvoiceEntity> findAllByImportDateBetween(Date start, Date end);
}
