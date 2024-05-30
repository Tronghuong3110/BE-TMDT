package com.javatechie.service;

import com.javatechie.dto.VoucherDto;
import org.json.simple.JSONObject;

import java.sql.Date;
import java.util.List;

public interface IVoucherService {
    JSONObject saveVoucher(VoucherDto voucherDto);
    List<VoucherDto> findAll(Date date);
    JSONObject findOne(Integer id);
    JSONObject updateVoucher(VoucherDto voucherDto);
    JSONObject deleteVoucher(Integer id);
    JSONObject createVoucherUser(Integer idVoucher);
    List<VoucherDto> findAllVoucherOfUser();
}
