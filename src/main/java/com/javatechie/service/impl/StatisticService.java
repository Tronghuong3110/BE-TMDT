package com.javatechie.service.impl;

import com.javatechie.entity.OrderEntity;
import com.javatechie.repository.OrderRepository;
import com.javatechie.service.IStatisticService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticService implements IStatisticService {

    @Autowired
    private OrderRepository orderRepository;
    @Override
    public List<JSONObject> findAllTopProductBestSeller() {
        return null;
    }

    @Override
    public JSONObject statisticProductSold(Date start, Date end) {
        // Lấy số lượng đã bán
        List<OrderEntity> orders = orderRepository.findAllByCreateDateBetween(start, end);

        return null;
    }
}
