package com.javatechie.service;

import com.javatechie.dto.PaymentDto;
import com.javatechie.entity.PaymentEntity;
import org.json.simple.JSONObject;

import java.util.List;

public interface IPaymentService {

    List<PaymentDto> findAllPayment();
    JSONObject savePayment(PaymentDto paymentDto);
}
