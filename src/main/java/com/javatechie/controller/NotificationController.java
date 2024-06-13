package com.javatechie.controller;


import com.javatechie.dto.NotificationDto;
import com.javatechie.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private INotificationService notificationService;

    @GetMapping("/notification")
    public List<NotificationDto> findAll() {
        return notificationService.findAll();
    }
}
