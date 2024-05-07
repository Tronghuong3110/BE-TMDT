package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.VoucherDto;
import com.javatechie.entity.User;
import com.javatechie.entity.UserVoucherEntity;
import com.javatechie.entity.VoucherEntity;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.repository.UserVoucherRepository;
import com.javatechie.repository.VoucherRepository;
import com.javatechie.service.IVoucherService;
import org.aspectj.weaver.ast.Literal;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@Service
public class VoucherService implements IVoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserVoucherRepository userVoucherRepository;
    @Override
    public JSONObject saveVoucher(VoucherDto voucherDto) {
        JSONObject response = new JSONObject();
        try {
            Boolean checkName = voucherRepository.existsByName(voucherDto.getName());
            if(checkName) {
                response.put("code", 0);
                response.put("message", "Name has been duplicated!!");
                return response;
            }
            if(voucherDto.getStartDate().compareTo(voucherDto.getEndDate()) > 0) {
                response.put("code", 0);
                response.put("message", "The start date cannot be greater than the end date!!");
                return response;
            }
            VoucherEntity voucher = new VoucherEntity();
//            voucher.setId(System.currentTimeMillis());
            BeanUtils.copyProperties(voucherDto, voucher);
            voucher.setDeleted(0);
            voucher.setNumberRemain(voucherDto.getNumberVoucher());
            voucher = voucherRepository.save(voucher);
            BeanUtils.copyProperties(voucher, voucherDto);
            response.put("code", 1);
            response.put("message", "Add new voucher success");
            response.put("voucher", voucherDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", e.getMessage());
            response.put("voucher", null);
        }
        return response;
    }

    @Override
    public List<VoucherDto> findAll() {
        try {
            System.out.println(new Date(System.currentTimeMillis()));
            List<VoucherEntity> listVouchers = voucherRepository.findById(null, new Date(System.currentTimeMillis()));
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
            List<VoucherEntity> voucher = voucherRepository.findById(id, new Date(System.currentTimeMillis()));
            VoucherDto voucherDto = new VoucherDto();
            if(voucher.size() <= 0) {
                response.put("code", 0);
                response.put("message", "Can not found voucher with id = " + id);
                return response;
            }
            BeanUtils.copyProperties(voucher.get(0), voucherDto);
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
            List<VoucherEntity> voucher = voucherRepository.findById(voucherDto.getId(), new Date(System.currentTimeMillis()));
            VoucherEntity voucherCheck = voucherRepository.findByNameAndDeleted(voucherDto.getName(), 0).orElse(null);
            if(voucherCheck != null && !voucherCheck.getId().equals(voucherDto.getId())) {
                response.put("code", 0);
                response.put("message", "Can not update voucher because of name duplicated");
                return response;
            }
            if(voucher.size() <= 0) {
                response.put("code", 0);
                response.put("message", "Can not found voucher with id = " + voucherDto.getId());
                return response;
            }
            VoucherEntity voucherentity = voucher.get(0);
            voucherentity = convertToEntity(voucherentity, voucherDto);
            if (voucherentity == null) {
                response.put("code", 0);
                response.put("message", "Update voucher error");
                return response;
            }
            voucherRepository.save(voucherentity);
            response.put("code", 1);
            response.put("message", "Update voucher success");
            BeanUtils.copyProperties(voucherentity, voucherDto);
            response.put("voucher", voucherDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Update voucher error");
            response.put("voucher", null);
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

    @Override
    public JSONObject createVoucherUser(Integer idVoucher) {
        JSONObject response = new JSONObject();
        try {
            VoucherEntity voucher = voucherRepository.findById(idVoucher).orElse(new VoucherEntity());
            Date currentDate = new Date(System.currentTimeMillis());
            if(currentDate.compareTo(voucher.getEndDate()) >= 1 || voucher.getNumberRemain() <= 0) {
                response.put("message", "Voucher expired");
                response.put("code", 0);
                response.put("userVoucher", null);
                return response;
            }
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            UserVoucherEntity userVoucherEntity = new UserVoucherEntity(System.currentTimeMillis(), new Date(System.currentTimeMillis()), voucher.getEndDate(), user, voucher, null);
            userVoucherEntity = userVoucherRepository.save(userVoucherEntity);
            voucher.setNumberRemain(voucher.getNumberRemain()-1);
            voucherRepository.save(voucher);
            response.put("code", 1);
            response.put("message", "Create voucher success");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Create voucher fail");
        }
        return response;
    }

    private VoucherEntity convertToEntity(VoucherEntity voucher, VoucherDto voucherDto) {
        try {
            BeanUtils.copyProperties(voucherDto, voucher);
            return voucher;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
