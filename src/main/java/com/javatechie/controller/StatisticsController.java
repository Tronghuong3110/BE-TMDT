package com.javatechie.controller;

import com.javatechie.service.IStatisticService;
import com.squareup.okhttp.Response;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin("*")
public class StatisticsController {

    @Autowired
    private IStatisticService statisticService;
    /*
    * - Số sp bán được trong tháng, doanh thu trong tháng, lợi nhuận, chi phí
    - Top 10 sp bán chạy nhất
    - Doanh thu/Lợi nhuận/ Chi phí trong năm (12 tháng) , theo quý (4 quý)
    * */
    @PostMapping("/statistic/month")
    public ResponseEntity<?> statisticByMonth(@RequestBody JSONObject request) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date start = Date.valueOf(request.get("start").toString());
        Date end = Date.valueOf(request.get("end").toString());
        JSONObject response = statisticService.statisticProductSold(start, end);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body("Lấy thống kê lỗi rồi !!");
        }
        return ResponseEntity.ok(response);
    }
}
