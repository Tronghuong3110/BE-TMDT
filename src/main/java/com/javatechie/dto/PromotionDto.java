package com.javatechie.dto;

import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
public class PromotionDto {
    private Integer id;
    private String content;
    private Date dateStart;
    private Date dateEnd;
    private Integer discount; // giảm bao nhiêu %
    private Integer deleted;
    private String pathImage;
    private List<Long> idItems; // sản phẩm chi tiết
    private List<Integer> idItemsRemove;
    private List<ProductItemDto> products;
}
