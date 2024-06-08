package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private Integer ranking;
    private Date dateReview;
    private String evaluate;
}
