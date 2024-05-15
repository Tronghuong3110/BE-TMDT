package com.javatechie.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private String unitNumber;
    private String streetNumber;
    private String city;
    private String region;
    private CountryDto country;
}
