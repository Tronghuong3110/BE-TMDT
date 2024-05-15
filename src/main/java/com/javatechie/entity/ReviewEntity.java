package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
public class ReviewEntity {

    @Id
    private Long id;
    @Column(name = "ranking")
    private Integer ranking;
    @Column(name = "date_review")
    private Date dateReview;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
