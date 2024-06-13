package com.javatechie.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Date createDate;
    private long totalPrice;
    private String statusPayment;
    private String nameUser;
    private String address;
    private ShipmentDto shipment;
    private PaymentDto payment;
    private Date datePayment;
    private String statusOrder;
    private List<CartItemDto> cartItems;
    private List<Integer> itemOrders; // danh sách id của cartItem
    private UserVoucherDto voucher;
}
