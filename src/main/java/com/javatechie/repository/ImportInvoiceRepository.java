package com.javatechie.repository;

import com.javatechie.entity.ImportInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportInvoiceRepository extends JpaRepository<ImportInvoiceEntity, Long> {
}
