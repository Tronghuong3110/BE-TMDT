package com.javatechie.service.impl;

import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.NotificationDto;
import com.javatechie.entity.NotificationEntity;
import com.javatechie.entity.User;
import com.javatechie.repository.NotifyRepository;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.service.INotificationService;
import com.javatechie.util.MapperUtil;
import org.aspectj.weaver.ast.Not;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService implements INotificationService {
    @Autowired
    private NotifyRepository notifyRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Override
    public List<NotificationDto> findAll() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userInfoUserDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userInfoUserDetails.getUsername(), 0).orElse(new User());
            List<NotificationEntity> listNotify = notifyRepository.findAllOrderByUnixTimeAndROle(user.getRoles().contains("USER") ? "USER" : "ADMIN");
            List<NotificationDto> responses = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(NotificationEntity notify : listNotify) {
                notify.setAck(1);
                notifyRepository.save(notify);
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

    @Override
    public Integer countNotification() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserInfoUserDetails userInfoUserDetails = (UserInfoUserDetails) auth.getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userInfoUserDetails.getUsername(), 0).orElse(new User());
            return notifyRepository.countAllByAckAndRole(0, user.getRoles().contains("ADMIN") ? "ADMIN" : "USER");
        }
        catch (Exception e) {
            return null;
        }
    }
}
