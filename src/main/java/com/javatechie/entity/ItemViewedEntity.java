package com.javatechie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_view")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemViewedEntity {
    @Id
    private Long id;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "viewed")
    private Integer viewed;
    @Column(name = "favorite")
    private Integer favorite;
}
