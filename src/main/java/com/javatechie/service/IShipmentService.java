package com.javatechie.service;

import com.javatechie.dto.ShipmentDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface IShipmentService {
    List<ShipmentDto> findAllShipment();
    JSONObject savePayment(ShipmentDto shipmentDto);
}
