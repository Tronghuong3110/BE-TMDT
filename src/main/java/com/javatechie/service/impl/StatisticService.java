package com.javatechie.service.impl;

import com.javatechie.service.IStatisticService;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService implements IStatisticService {
    @Override
    public List<JSONObject> findAllTopProductBestSeller() {
        return null;
    }
}
