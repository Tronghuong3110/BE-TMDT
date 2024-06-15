package com.javatechie.service.impl;

import com.javatechie.entity.CartItemEntity;
import com.javatechie.entity.OrderEntity;
import com.javatechie.entity.ProductItemInvoiceEntity;
import com.javatechie.repository.CartItemRepository;
import com.javatechie.repository.OrderRepository;
import com.javatechie.repository.ProductItemInvoiceRepository;
import com.javatechie.service.IStatisticService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StatisticService implements IStatisticService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductItemInvoiceRepository productItemInvoiceRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override // lấy danh sách 10 sản phâẩm bán chạy nhất
    public List<JSONObject> findAllTopProductBestSeller() {
        try {
            List<JSONObject> responses = new ArrayList<>();
            List<JSONObject> topProduct = cartItemRepository.statisticTopProduct();
            JSONParser parser = new JSONParser();
            for(JSONObject obj : topProduct) {
                JSONObject product = (JSONObject) parser.parse(obj.get("product_info").toString());
                responses.add(product);
            }
            return responses;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override // tính tổng số lượng đã bán trong tháng, lợi nhuận, chi phí, doanh thu trong tháng
    public JSONObject statisticProductSold(Date start, Date end) {
        try {
            JSONObject response = new JSONObject();
            // Lấy số lượng đã bán
            // từ danh sách order ==> số lượng sản phẩm đã bán được
            List<OrderEntity> orders = orderRepository.findAllByCreateDateBetweenAndStatus(start, end);
            // tổng số lượng bán được
            long quantitySold = orders.stream()
                                .flatMap(orderEntity -> orderEntity.getCartItems().stream())
                                .mapToLong(CartItemEntity :: getQuantity)
                                .sum();
            // Tổng tiền bán được (doanh thu)
            long totalPrice = orders.stream().mapToLong(OrderEntity::getTotalPrice).sum();
            // tính tổng tiền hàng nhập được trong tháng (chi phí)
            List<ProductItemInvoiceEntity> invoices = productItemInvoiceRepository.findAllByImportDateBetween(start, end);
            long cost = invoices.stream().mapToLong(invoice ->(long)(invoice.getCost() * invoice.getQuantity())).sum();
            long profit = totalPrice - cost; // lợi nhuận
            response.put("cost", cost);
            response.put("profit", profit);
            response.put("totalPrice", totalPrice);
            response.put("quantitySold", quantitySold);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject statisticProductSoldByYear(Integer year) {
        JSONObject response = new JSONObject();
        try {
            List<JSONObject> totalRevenue = new ArrayList<>();
            List<JSONObject> totalCost = new ArrayList<>();
            List<JSONObject> totalProfit = new ArrayList<>();
            for(int i = 1; i <= 12; i++) {
                String month = year + "-" + (i < 10 ? "0"+i : i);
                JSONObject tmp = new JSONObject();
                tmp.put("total", 0);
                tmp.put("months", month);
                // lợi nhuận
                JSONObject revenue = orderRepository.calculatorTotalRevenueByMonth(month).orElse(tmp);
                totalRevenue.add(revenue);
                // chi phí
                JSONObject cost = productItemInvoiceRepository.calculatorCostByMonth(month).orElse(tmp);
                totalCost.add(cost);
                // tính lợi nhuận
                double profit = Double.parseDouble(revenue.get("total").toString()) - Double.parseDouble(cost.get("total").toString());
                JSONObject profitTmp = new JSONObject(tmp);
                profitTmp.put("total", profit);
                totalProfit.add(profitTmp);
                System.out.println(cost);
            }
            response.put("revenue", totalRevenue);
            response.put("cost", totalCost);
            response.put("profit", totalProfit);
            response.put("code", 1);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Thống kê lỗi !!");
        }
        return response;
    }
}
