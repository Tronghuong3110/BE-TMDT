package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Table(name = "user")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "address")
    private String address;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "modified_date")
    private Date modifiedDate;
    @Column(name = "roles")
    private String roles;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "dob")
    private Date dob;
    @Column(name = "avatar_path")
    private String avatarPath;

    @OneToOne(mappedBy = "user")
    private CartEntity cart;
    @OneToMany(mappedBy = "user")
    List<OrderEntity> orders;
    @OneToMany(mappedBy = "user")
    List<ReviewEntity> reviews;
    @OneToMany(mappedBy = "user")
    private List<NotificationEntity> notifies;
    @OneToMany(mappedBy = "user")
    private List<UserVoucherEntity> userVouchers;
}
