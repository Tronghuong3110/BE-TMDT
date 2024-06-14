package com.javatechie.controller;

import com.javatechie.dto.OrderDto;
import com.javatechie.service.IOrderService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/orders")
    public List<OrderDto> findAll(@RequestParam("status")Optional<Integer> status) {
        return orderService.findAllOrder(status.orElse(null));
    }

    @GetMapping("/order")
    public JSONObject findAllItemOfOrder(@RequestParam("orderId")long orderId) {
        return orderService.getListItemOfOrder(orderId);
    }

    // THANH TOÁN ONLINE
    @PostMapping("/payment/online")
    public ResponseEntity<?> createPayment(@RequestBody OrderDto orderDto) {
        JSONObject response = orderService.paymentOnline(orderDto);
        if(response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    // http://localhost:8080/vnpay_jsp/vnpay_return.jsp?vnp_Amount=1000000&vnp_BankCode=NCB&vnp_BankTranNo=VNP14401189&vnp_CardType=ATM&vnp_OrderInfo=Thanh+toan+don+hang%3A74699639&vnp_PayDate=20240504003121&vnp_ResponseCode=00&vnp_TmnCode=E4JQU9CS&vnp_TransactionNo=14401189&vnp_TransactionStatus=00&vnp_TxnRef=74699639&vnp_SecureHash=f26acac3550a9f162776310f3e3589943a7efb94ee875021ed649f60e543f2570b608bd30c283f7eda6a3f25fdb97d44cafeef0a41e70ed8421b1a871fe7c0d6
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto, @RequestParam("vnp_BankTranNo") String bankTranNo,
                                         @RequestParam("vnp_OrderInfo") Optional<String> orderInfo,
                                         @RequestParam("vnp_ResponseCode") Optional<String> responseCode,
                                         @RequestParam("vnp_TransactionStatus") Optional<String> transactionCode,
                                         @RequestParam("shipmentId") Long shipmentId,
                                         @RequestParam("paymentId") Long paymentId,
                                         @RequestParam("voucherId") Optional<Long> voucherId) {
        // TH thanh toán shipcode ==> bankTranNo = shipCode
        JSONObject response = orderService.saveOrder(orderDto, shipmentId, paymentId, voucherId.orElse((long)0), orderInfo.orElse(null), responseCode.orElse(null),
                                                        transactionCode.orElse(null), bankTranNo);
        if (response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/order")
    public ResponseEntity<?> updateOrder(@RequestParam("status") Optional<Integer> status,
                                         @RequestParam("cancel") Optional<Integer> cancel,
                                         @RequestParam("orderId") Long orderId) {
        JSONObject response = orderService.updateOrder(status.orElse(0), cancel.orElse(null), orderId);
        if (response.get("code").equals(0)) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
