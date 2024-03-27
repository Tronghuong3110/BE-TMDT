package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;
}
