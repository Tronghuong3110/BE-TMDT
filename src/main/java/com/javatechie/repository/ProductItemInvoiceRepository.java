package com.javatechie.repository;

import com.javatechie.entity.ProductItemEntity;
import com.javatechie.entity.ProductItemInvoiceEntity;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductItemInvoiceRepository extends JpaRepository<ProductItemInvoiceEntity, Long> {
    List<ProductItemInvoiceEntity> findAllByImportDateBetween(Date start, Date end);

    @Query(value = "select sum(cost * quantity) as total, date_format(import_date, \"%Y-%m\") as months from product_item_invoice\n" +
                    "where date_format(import_date, \"%Y-%m\") = :month \n" +
                    "group by months;", nativeQuery = true)
    Optional<JSONObject> calculatorCostByMonth(@Param("month") String month);
}
