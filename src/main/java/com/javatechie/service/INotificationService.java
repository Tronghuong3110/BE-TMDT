package com.javatechie.service;

import com.javatechie.dto.NotificationDto;
import org.json.simple.JSONObject;

import java.util.List;

public interface INotificationService {
    List<NotificationDto> findAll();
    JSONObject updateAck(Long notificationId);

    Integer countNotification();
}
