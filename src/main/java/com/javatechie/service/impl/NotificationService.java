package com.javatechie.service.impl;

import com.javatechie.dto.NotificationDto;
import com.javatechie.entity.NotificationEntity;
import com.javatechie.repository.NotifyRepository;
import com.javatechie.service.INotificationService;
import com.javatechie.util.MapperUtil;
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
}
