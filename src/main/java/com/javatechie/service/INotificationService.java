package com.javatechie.service;

import com.javatechie.dto.NotificationDto;

import java.util.List;

public interface INotificationService {

    List<NotificationDto> findAll();
}
