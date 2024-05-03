package com.javatechie.service.impl;

import com.javatechie.config.ConfigPaymentOnline;
import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.CartItemDto;
import com.javatechie.dto.ItemDto;
import com.javatechie.dto.OrderDto;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IOrderService;
import org.json.simple.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private UserVoucherRepository userVoucherRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    // lấy ra danh sách đơn hàng đã đặt (dành cho admin)
    @Override
    public List<OrderDto> findAllOrder() {
        try {
            List<OrderEntity> listOrder = orderRepository.findAll();
            List<OrderDto> listResponse = new ArrayList<>();
            for(OrderEntity order : listOrder) {
                OrderDto orderDto = new OrderDto();
                BeanUtils.copyProperties(order, orderDto);
                String nameUser = order.getUser().getName();
                orderDto.setNameUser(nameUser);
                listResponse.add(orderDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // lấy ra danh sách các sản phẩm của 1 đơn hàng
    @Override
    public List<CartItemDto> getListItemOfOrder(Long orderId) {
        try {
            OrderEntity order = orderRepository.findById(orderId).orElse(null);
            List<CartItemEntity> listCartItem = order.getCartItems();
            List<CartItemDto> listResponse = new ArrayList<>();
            for(CartItemEntity cartItem : listCartItem) {
                CartItemDto cartItemDto = new CartItemDto();
                BeanUtils.copyProperties(cartItem, cartItemDto);
                ItemDetailEntity item = cartItem.getItem();
                ItemDto itemDto = new ItemDto();
                BeanUtils.copyProperties(item, itemDto);
                cartItemDto.setItemDto(itemDto);
                listResponse.add(cartItemDto);
            }
            return listResponse;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // đặt hàng
    @Override
    public JSONObject saveOrder(OrderDto orderDto, Long shipmentMethod, Long paymentMethod, Long voucherId, String orderInfo, String responseCode, String transactionCode, String bankTranNo) {
        JSONObject response = new JSONObject();
        try {
            // status: 0-chưa thanh toán, 1-chờ xử lý, 2-đã thanh toán
            // Lấy ra method thanh toán
            PaymentEntity payment = paymentRepository.findById(paymentMethod).orElse(new PaymentEntity());
            String statusPayment;
            Date datePayment = null;
            if(payment.getCode().equals("cash")) {
                statusPayment = "Chưa thanh toán";
            }
            else {
                statusPayment = transactionCode.equals("00")&&responseCode.equals("00")?"Đã thanh toán thành công":"Thanh toán thất bại";
                datePayment = new Date(System.currentTimeMillis());
            }
            // lấy ra method vận chuyển
            ShipmentEntity shipment = shipmentRepository.findById(shipmentMethod).orElse(new ShipmentEntity());
            // lấy ra voucher của user
            UserVoucherEntity voucher = userVoucherRepository.findById(voucherId).orElse(new UserVoucherEntity());
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());

            OrderEntity order = new OrderEntity(System.currentTimeMillis(), new Date(System.currentTimeMillis()), orderDto.getTotalPrice(),
                                                null, statusPayment, orderDto.getAddress(), "Đã nhận đơn hàng", datePayment, orderInfo, bankTranNo, user,
                                                null, payment, shipment, voucher);
            order = orderRepository.save(order);
            // lấy ra danh sách các item người dùng muốn đặt hàng
            for(Integer idCartItem : orderDto.getItemOrders()) {
                CartItemEntity cartItem = cartItemRepository.findById(idCartItem).orElse(new CartItemEntity());
                cartItem.setOrder(order);
                cartItemRepository.save(cartItem);
            }
            BeanUtils.copyProperties(order, orderDto);
            response.put("code", 1);
            response.put("message", "Order success!!");
            response.put("order", orderDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Order fail!!");
            response.put("order", new OrderDto());
        }
        return response;
    }

    // Tạo ra đường dẫn tới trang thanh toán
    @Override
    public JSONObject paymentOnline(OrderDto order) {
        JSONObject response = new JSONObject();
        try {
            // Thực hiện thanh toán online
            long amount = order.getTotalPrice()*100;
            String bankCode = "NCB";
            String vnp_TxnRef = ConfigPaymentOnline.getRandomNumber(8);
//            String vnp_IpAddr = Config.getIpAddress(req);
            String vnp_TmnCode = ConfigPaymentOnline.vnp_TmnCode;
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", ConfigPaymentOnline.vnp_Version);
            vnp_Params.put("vnp_Command", ConfigPaymentOnline.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
//            if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
//            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", ConfigPaymentOnline.orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", ConfigPaymentOnline.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", "192.168.0.35");

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = ConfigPaymentOnline.hmacSHA512(ConfigPaymentOnline.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = ConfigPaymentOnline.vnp_PayUrl + "?" + queryUrl;
            response.put("code", 1);
            response.put("url", paymentUrl);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("url", "");
        }
        return response;
    }
}
