package com.javatechie.service.impl;

import com.javatechie.dto.NotificationDto;
import com.javatechie.entity.NotificationEntity;
import com.javatechie.repository.NotifyRepository;
import com.javatechie.service.INotificationService;
import com.javatechie.util.MapperUtil;
import org.aspectj.weaver.ast.Not;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private NotifyRepository notifyRepository;
    @Override
    public List<NotificationDto> findAll() {
        try {
            List<NotificationEntity> listNotify = notifyRepository.findAllOrderByUnixTime();
            List<NotificationDto> responses = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(NotificationEntity notify : listNotify) {
                NotificationDto notificationDto = new NotificationDto();
                mapper.map(notify, notificationDto);
                responses.add(notificationDto);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject updateAck(Long notificationId) {
        JSONObject response = new JSONObject();
        try {
            NotificationEntity notificationEntity = notifyRepository.findById(notificationId).orElse(new NotificationEntity());
            notificationEntity.setAck(1); // đánh dấu đã đọc
            notifyRepository.save(notificationEntity);
            response.put("code", 1);
            response.put("message", "Cập nhật thành công !!");
            return  response;
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật thất bại !!");
        }
        return response;
    }
}
