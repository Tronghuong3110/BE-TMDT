package com.javatechie.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_address")
public class UserAddressEntity {

    @Id
    private Long id;
    @Column(name = "id_default")
    private boolean idDefault;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
