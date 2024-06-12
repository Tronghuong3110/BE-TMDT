package com.javatechie.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.json.simple.JSONObject;

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
    private List<Long> idItemsRemove;
    private List<JSONObject> products;
}
