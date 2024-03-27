package com.javatechie.service;

import com.javatechie.dto.VoucherDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IVoucherService {
    JSONObject saveVoucher(VoucherDto voucherDto);
    List<VoucherDto> findAll();
    JSONObject findOne(Integer id);
    JSONObject updateVoucher(VoucherDto voucherDto);
    JSONObject deleteVoucher(Integer id);
}
