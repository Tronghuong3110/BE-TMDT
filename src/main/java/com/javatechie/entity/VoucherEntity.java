package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name = "voucher")
public class VoucherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "discount")
    private Integer discount; // giam bao nhiêu %
    @Column(name = "discountConditions")
    private Integer discountConditions; // điều kiện áp dụng giảm giá
    @Column(name = "number_voucher")
    private Integer numberVoucher; // số lượng phiếu giảm giá ban hành
    @Column(name = "deleted")
    private Integer deleted;

    @OneToMany(mappedBy = "voucher")
    private List<UserVoucherEntity> userVouchers;
}
