package com.javatechie.service;

import com.javatechie.dto.SupplierDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface ISupplierService {
    JSONObject saveSupplier(SupplierDto supplierDto);
    SupplierDto findIOneSupplier(long id);
    List<SupplierDto> findAllSupplier();
    JSONObject deleteSupplier(long id);
    JSONObject updateSupplier(SupplierDto supplierDto);
}
