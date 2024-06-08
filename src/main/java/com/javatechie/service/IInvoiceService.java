package com.javatechie.service;

import com.javatechie.dto.InvoiceDto;

import java.sql.Date;
import java.util.List;

public interface IInvoiceService {
    List<InvoiceDto> findAllInvoice(Date start, Date end);
    InvoiceDto findOneById(Long id);
}
