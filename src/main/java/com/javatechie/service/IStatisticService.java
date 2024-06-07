package com.javatechie.service;

import org.json.simple.JSONObject;

import java.util.List;

public interface IStatisticService {

    List<JSONObject> findAllTopProductBestSeller();
}
