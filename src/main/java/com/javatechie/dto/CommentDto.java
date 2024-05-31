package com.javatechie.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Date;

@Data
public class CommentDto {
    private Long id;
    private Date createDate;
    private Date modifiledDate;
    private String content;
    private UserDto user;
    private String fullName;
    private String avatarPath;
}
