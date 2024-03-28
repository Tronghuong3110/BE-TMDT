package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ranking")
    private Integer ranking;
    @Column(name = "comment", columnDefinition = "text")
    private String comment;
    @Column(name = "date_review")
    private Date dateReview;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
