package com.javatechie.dto;

import com.javatechie.entity.ItemInvoiceEntity;
import com.javatechie.entity.SupplierEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ImportInvoiceDto {
    private Long id;
    private Date importDate;
    List<ItemInvoiceDto> itemInvoices;
    private SupplierDto supplier;
}
