package com.javatechie.repository;

import com.javatechie.entity.ItemInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemInvoiceRepository extends JpaRepository<ItemInvoiceEntity, Long> {
}
