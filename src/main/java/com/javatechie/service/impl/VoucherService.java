package com.javatechie.service.impl;

import com.javatechie.dto.VoucherDto;
import com.javatechie.entity.VoucherEntity;
import com.javatechie.repository.VoucherRepository;
import com.javatechie.service.IVoucherService;
import org.aspectj.weaver.ast.Literal;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoucherService implements IVoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Override
    public JSONObject saveVoucher(VoucherDto voucherDto) {
        JSONObject response = new JSONObject();
        try {
            Boolean checkName = voucherRepository.existsByName(voucherDto.getName());
            if(checkName) {
                response.put("code", 0);
                response.put("message", "Name has been duplicated");
                return response;
            }
            VoucherEntity voucher = new VoucherEntity();
            BeanUtils.copyProperties(voucherDto, voucher);
            voucher.setDeleted(0);
            voucherRepository.save(voucher);
            response.put("code", 1);
            response.put("message", "Add new voucher success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @Override
    public List<VoucherDto> findAll() {
        try {
            List<VoucherEntity> listVouchers = voucherRepository.findAllByDeleted(0);
            List<VoucherDto> listResponse = new ArrayList<>();
            for(VoucherEntity voucher : listVouchers) {
                VoucherDto voucherDto = new VoucherDto();
                BeanUtils.copyProperties(voucher, voucherDto);
                listResponse.add(voucherDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject findOne(Integer id) {
        JSONObject response = new JSONObject();
        try {
            VoucherEntity voucher = voucherRepository.findByIdAndDeleted(id, 0).orElse(null);
            VoucherDto voucherDto = new VoucherDto();
            if(voucher == null) {
                response.put("code", 0);
                response.put("message", "Can not found voucher with id = " + id);
                return response;
            }
            BeanUtils.copyProperties(voucher, voucherDto);
            response.put("code", 1);
            response.put("message", voucherDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Find voucher by id error");
        }
        return response;
    }

    @Override
    public JSONObject updateVoucher(VoucherDto voucherDto) {
        JSONObject response = new JSONObject();
        try {
            VoucherEntity voucher = voucherRepository.findByIdAndDeleted(voucherDto.getId(), 0).orElse(null);
            VoucherEntity voucherCheck = voucherRepository.findByNameAndDeleted(voucherDto.getName(), 0).orElse(null);
            if(voucherCheck != null && !voucherCheck.getId().equals(voucherDto.getId())) {
                response.put("code", 0);
                response.put("message", "Can not update voucher because of name duplicated");
                return response;
            }
            if(voucher == null) {
                response.put("code", 0);
                response.put("message", "Can not found voucher with id = " + voucherDto.getId());
                return response;
            }
            voucher = convertToEntity(voucher, voucherDto);
            if (voucher == null) {
                response.put("code", 0);
                response.put("message", "Update voucher error");
                return response;
            }
            voucherRepository.save(voucher);
            response.put("code", 1);
            response.put("message", "Update voucher success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Update voucher error");
        }
        return response;
    }

    @Override
    public JSONObject deleteVoucher(Integer id) {
        JSONObject response = new JSONObject();
        try {
            VoucherEntity voucher = voucherRepository.findById(id).orElse(null);
            if(voucher == null) {
                response.put("code", 0);
                response.put("message", "Can not found voucher with id = " + id);
                return response;
            }
            voucher.setDeleted(1);
            voucherRepository.save(voucher);
            response.put("code", 1);
            response.put("message", "Delete voucher success");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private VoucherEntity convertToEntity(VoucherEntity voucher, VoucherDto voucherDto) {
        try {
            if(voucherDto.getName() != null) {
                voucher.setName(voucher.getName());
            }
            if(voucherDto.getNumberVoucher() != null) {
                voucher.setNumberVoucher(voucherDto.getNumberVoucher());
            }
            if(voucherDto.getDescription() != null) {
                voucher.setDescription(voucherDto.getDescription());
            }
            if(voucherDto.getDiscount() != null) {
                voucher.setDiscount(voucherDto.getDiscount());
            }
            if(voucherDto.getDiscountConditions() != null) {
                voucher.setDiscountConditions(voucherDto.getDiscountConditions());
            }
            return voucher;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
