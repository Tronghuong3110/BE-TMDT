package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "user_voucher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherEntity {

    @Id
    private Long id;
    @Column(name = "date_start")
    private Date dateStart;
    @Column(name = "date_end")
    private Date dateEnd;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private VoucherEntity voucher;
    @OneToOne(mappedBy = "voucher", fetch = FetchType.LAZY)
    private OrderEntity order;
}
