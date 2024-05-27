package com.javatechie.service.impl;

import com.javatechie.dto.ShipmentDto;
import com.javatechie.entity.ShipmentEntity;
import com.javatechie.repository.ShipmentRepository;
import com.javatechie.service.IShipmentService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService implements IShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;
    @Override
    public List<ShipmentDto> findAllShipment() {
        List<ShipmentEntity> listShipments = shipmentRepository.findAll();
        List<ShipmentDto> shipments = new ArrayList<>();
        ModelMapper mapper = MapperUtil.configModelMapper();
        for(ShipmentEntity shipment : listShipments) {
            ShipmentDto shipmentDto = new ShipmentDto();
            mapper.map(shipment, shipmentDto);
            shipments.add(shipmentDto);
        }
        return shipments;
    }

    @Override
    public JSONObject savePayment(ShipmentDto shipmentDto) {
        JSONObject response = new JSONObject();
        try {
            ShipmentEntity shipment = shipmentRepository.findByName(shipmentDto.getName()).orElse(new ShipmentEntity());
            if(shipment.getId() != null) {
                response.put("code", 0);
                response.put("message", "Phương thức giao hàng đã tồn tại !");
                response.put("shipment", null);
            }
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(shipmentDto, shipment);
            shipment = shipmentRepository.save(shipment);
            response.put("code", 1);
            response.put("message", "Thêm mới phương thức thanh toán thành công!");
            mapper.map(shipment, shipmentDto);
            response.put("payment", shipmentDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thêm mới phương thức giao hàng thất bại !");
            response.put("shipment", null);
        }
        return response;
    }
}
