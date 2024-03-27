package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
public class PromotionDto {
    private Integer id;
    private String content;
    private Date dateStart;
    private Date dateEnd;
    private Integer discount; // giam bao nhiêu %
    private Integer discountConditions;
    private List<Integer> idItems = new ArrayList<>();
}
