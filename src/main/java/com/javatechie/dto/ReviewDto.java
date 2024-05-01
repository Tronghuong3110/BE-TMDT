package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class ReviewDto {
    private Integer id;
    private Integer ranking;
    private Date dateReview;
}
