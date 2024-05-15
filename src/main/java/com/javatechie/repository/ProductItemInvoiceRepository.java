package com.javatechie.repository;

import com.javatechie.entity.ProductItemEntity;
import com.javatechie.entity.ProductItemInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemInvoiceRepository extends JpaRepository<ProductItemInvoiceEntity, Long> {
}
