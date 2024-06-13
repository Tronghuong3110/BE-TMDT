package com.javatechie.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class UserVoucherDto {
    private Long id;
    private Date dateStart;
    private Date dateEnd;
    private Boolean used;
    private VoucherDto voucher;
}
