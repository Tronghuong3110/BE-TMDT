package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "promotion")
@Data
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "date_start")
    private Date dateStart;
    @Column(name = "date_end")
    private Date dateEnd;
    @Column(name = "discount")
    private Integer discount; // giảm bao nhiêu %
    @Column(name = "deleted")
    private Integer deleted;

    @OneToMany(mappedBy = "promotion")
    private List<ItemDetailEntity> items;

    @OneToMany(mappedBy = "promotion")
    private List<NotificationEntity> notifications;
}
