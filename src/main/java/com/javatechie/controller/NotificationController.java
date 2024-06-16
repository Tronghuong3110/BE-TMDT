package com.javatechie.controller;


import com.javatechie.dto.NotificationDto;
import com.javatechie.service.INotificationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/notification")
    public ResponseEntity<?> updateNotification(@RequestParam("id") Long notificationId) {
        JSONObject response = notificationService.updateAck(notificationId);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/notification")
    public ResponseEntity<?> countNotification() {
        Integer count = notificationService.countNotification();
        if(count == null) return ResponseEntity.badRequest().body("Lỗi");
        return ResponseEntity.ok(count);
    }
}
