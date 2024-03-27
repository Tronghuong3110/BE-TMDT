package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;
import java.sql.Date;

@Data
public class UserDto {
    private Integer id;
    private String username;
    private String name;
    private String email;
    private String password;
    private String address;
    private Date createDate;
    private Date modifiedDate;
    private String roles;
}
