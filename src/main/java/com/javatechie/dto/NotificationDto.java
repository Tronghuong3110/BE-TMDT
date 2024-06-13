package com.javatechie.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private Long id;
    private String description;
    private Integer ack;
    private Long orderId;
    private Long unixTime;
}
