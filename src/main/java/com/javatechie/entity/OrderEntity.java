package com.javatechie.entity;

import com.fasterxml.jackson.annotation.JacksonInject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.sql.Date;
import java.util.List;

@Table(name = "orders")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    private Long id;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "promotion_money")
    private Double promotionMoney;
    @Column(name = "status") // trạng thái thanh toán
    private String statusPayment;
    @Column(name = "address", columnDefinition = "text")
    private String address;
    @Column(name = "status_order")
    private String statusOrder;
    @Column(name = "date_payment")
    private Date datePayment;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "bank_tran_no")
    private String bankTranNo;
    @Column(name = "status_order_int")
    private Integer statusOrderInt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItems;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;
    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private ShipmentEntity shipment;
    @OneToOne
    @JoinColumn(name = "id_voucher_user")
    private UserVoucherEntity voucher;
}
