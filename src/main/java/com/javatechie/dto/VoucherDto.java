package com.javatechie.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class VoucherDto {
    private Integer id;
    private String name;
    private String description;
    private Integer discount;
    private Integer discountConditions;
    private Integer numberVoucher;
    private Integer deleted;
    List<Integer> idItems;
    private Date startDate;
    private Date endDate;
    private Integer numberRemain;
    private Long userVoucherId;
}
