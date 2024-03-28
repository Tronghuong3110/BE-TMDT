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
    private Integer discount; // giảm bao nhiêu %
    private Integer deleted;
    private List<Integer> idItems = new ArrayList<>();
}
