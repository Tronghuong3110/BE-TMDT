package com.javatechie.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String passwordOld;
    private String address;
    private Date createDate;
    private Date modifiedDate;
    private String roles;
    private String phoneNumber;
    private Date dob;
    private String avatarPath;
}
