package com.javatechie.dto;

import lombok.Data;

@Data
public class UserAddressDto {
    private Long id;
    private boolean idDefault;
    private AddressDto address;
}
