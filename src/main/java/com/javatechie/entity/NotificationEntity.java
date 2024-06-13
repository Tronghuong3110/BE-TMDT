package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification")
public class NotificationEntity {
    @Id
    private Long id;
    @Column(name = "description", columnDefinition = "longtext")
    private String description;
    @Column(name = "ack")
    private Integer ack;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "unix_time")
    private Long unixTime;
}
