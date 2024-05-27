package com.javatechie.service.impl;

import com.javatechie.dto.PaymentDto;
import com.javatechie.entity.PaymentEntity;
import com.javatechie.repository.PaymentRepository;
import com.javatechie.service.IPaymentService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<PaymentDto> findAllPayment() {
        List<PaymentEntity> listPayments = paymentRepository.findAll();
        List<PaymentDto> payments = new ArrayList<>();
        ModelMapper mapper = MapperUtil.configModelMapper();
        for(PaymentEntity payment : listPayments) {
            PaymentDto paymentDto = new PaymentDto();
            mapper.map(payment, paymentDto);
            payments.add(paymentDto);
        }
        return payments;
    }

    @Override
    public JSONObject savePayment(PaymentDto paymentDto) {
        JSONObject response = new JSONObject();
        try {
            PaymentEntity payment = paymentRepository.findByName(paymentDto.getName()).orElse(new PaymentEntity());
            if(payment.getId() != null) {
                response.put("code", 0);
                response.put("message", "Phương thức thanh toán đã tồn tại !");
                response.put("payment", null);
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(paymentDto, payment);
            payment = paymentRepository.save(payment);
            response.put("code", 1);
            response.put("message", "Thêm mới phương thức thanh toán thành công!");
            mapper.map(payment, paymentDto);
            response.put("payment", paymentDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới phương thức thanh toán thất bại !");
            response.put("payment", null);
        }
        return response;
    }
}
