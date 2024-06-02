package com.javatechie.service.impl;

import com.javatechie.dto.SupplierDto;
import com.javatechie.entity.SupplierEntity;
import com.javatechie.repository.SupplierRepository;
import com.javatechie.service.ISupplierService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SupplierService implements ISupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public JSONObject saveSupplier(SupplierDto supplierDto) {
        JSONObject response = new JSONObject();
        try {
            boolean validatePhoneNumber = checkValidatePhoneNumber(supplierDto.getPhoneNumber());
            if(!validatePhoneNumber) {
                response.put("code", 0);
                response.put("message", "Số điện thoại không hợp lệ !!");
                return response;
            }
            boolean existsNameSupplier = supplierRepository.existsByName(supplierDto.getName());
            if(existsNameSupplier) {
                response.put("code", 0);
                response.put("message", "Tên nhà cung cấp đã tồn tại !!");
                return response;
            }
            boolean existsPhoneNumber = supplierRepository.existsByPhoneNumber(supplierDto.getPhoneNumber());
            if(existsPhoneNumber) {
                response.put("code", 0);
                response.put("message", "Số điện thoại nhà cung cấp đã tồn tại !!");
                return response;
            }
            SupplierEntity supplier = new SupplierEntity();
            BeanUtils.copyProperties(supplierDto, supplier);
            supplier.setDeleted(0);
            supplier.setId(System.currentTimeMillis());
            supplier = supplierRepository.save(supplier);
            BeanUtils.copyProperties(supplier, supplierDto);
            response.put("code", 1);
            response.put("message", "Thêm mới nhà cung cấp thành công");
            response.put("supplier", supplierDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới nhà cung cấp thất bại !!");
        }
        return response;
    }

    @Override
    public SupplierDto findIOneSupplier(long id) {
        try {
            SupplierEntity supplier = supplierRepository.findByIdAndDeleted(id, 0).orElse(new SupplierEntity());
            SupplierDto supplierDto = new SupplierDto();
            BeanUtils.copyProperties(supplier, supplierDto);
            return supplierDto;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new SupplierDto();
    }

    @Override
    public List<SupplierDto> findAllSupplier() {
        try {
            List<SupplierEntity> listSuppliers = supplierRepository.findAllByDeleted(0);
            List<SupplierDto> responses = new ArrayList<>();
            for(SupplierEntity supplier : listSuppliers) {
                SupplierDto supplierDto = new SupplierDto();
                BeanUtils.copyProperties(supplier, supplierDto);
                responses.add(supplierDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject deleteSupplier(long id) {
        JSONObject response = new JSONObject();
        try {
            SupplierEntity supplier = supplierRepository.findByIdAndDeleted(id, 0).orElse(new SupplierEntity());
            supplier.setDeleted(1);
            supplierRepository.save(supplier);
            response.put("code", 1);
            response.put("message", "Xóa nhà cung cấp thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Xóa nhà cung cấp thất bại !!");
        }
        return response;
    }

    @Override
    public JSONObject updateSupplier(SupplierDto supplierDto) {
        JSONObject response = new JSONObject();
        try {
            SupplierEntity oldSupplier = supplierRepository.findByIdAndNameOrPhoneNumberAndDeleted(supplierDto.getId(), supplierDto.getName(), supplierDto.getPhoneNumber(), 0).orElse(null);
            if(oldSupplier != null) {
                response.put("code", 0);
                response.put("message", "Tên hoặc số điện thoại đã tồn tại !!");
                return response;
            }
            boolean validatePhoneNumber = true;
            if(supplierDto.getPhoneNumber() != null) {
                validatePhoneNumber = checkValidatePhoneNumber(supplierDto.getPhoneNumber());
            }
            if(!validatePhoneNumber) {
                response.put("code", 0);
                response.put("message", "Số điện thoại không hợp lệ !!");
                return response;
            }
            oldSupplier = supplierRepository.findByIdAndDeleted(supplierDto.getId(), 0).orElse(new SupplierEntity());
            BeanUtils.copyProperties(supplierDto, oldSupplier);
            supplierRepository.save(oldSupplier);
            BeanUtils.copyProperties(oldSupplier, supplierDto);
            response.put("code", 1);
            response.put("message", "Cập nhật thông tin nhà cung cấp thành công !!");
            response.put("supplier", supplierDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thông tin nhà cung cấp thất bại !!");
        }
        return response;
    }

    private boolean checkValidatePhoneNumber(String phoneNumber) {
//        if(phoneNumber.length() <= 8) return false;
        Pattern pattern = Pattern.compile("(0[3|5|7|8|9])+([0-9]{8})\\b");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.find();
    }
}
