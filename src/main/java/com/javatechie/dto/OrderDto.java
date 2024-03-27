package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Date;

@Data
public class OrderDto {
    private Integer id;
    private Date createDate;
    private Double totalPrice;
    private Integer shippingPrice;
    private Double promotionMoney;
    private String status;
    private String nameUser;
    private Integer cartId;
}
