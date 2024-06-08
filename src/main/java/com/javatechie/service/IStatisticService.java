package com.javatechie.service;

import org.json.simple.JSONObject;

import java.sql.Date;
import java.util.List;

public interface IStatisticService {

    List<JSONObject> findAllTopProductBestSeller();
    JSONObject statisticProductSold(Date start, Date end);
}
