package com.javatechie.service.impl;

import com.javatechie.config.ConfigPaymentOnline;
import com.javatechie.config.UserInfoUserDetails;
import com.javatechie.dto.*;
import com.javatechie.entity.*;
import com.javatechie.repository.*;
import com.javatechie.service.IOrderService;
import com.javatechie.util.AlertNotify;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
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
    @Autowired
    private ProductItemRepository productItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private NotifyRepository notifyRepository;

    // lấy ra danh sách đơn hàng đã đặt (dành cho người dùng và admin)
    @Override
    public List<OrderDto> findAllOrder(Integer status) {
        try {
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            List<OrderEntity> listOrder = new ArrayList<>();
            if(user.getRoles().contains("ADMIN")) {
                listOrder = orderRepository.findAllOrder(status);
            }
            else {
                listOrder = orderRepository.findAllByUserId(user.getId(), status);
            }
            List<OrderDto> listResponse = new ArrayList<>();
            for(OrderEntity order : listOrder) {
                OrderDto orderDto = new OrderDto();
                BeanUtils.copyProperties(order, orderDto);
                String nameUser = order.getUser().getName();
                orderDto.setNameUser(nameUser);
                // lấy ra shipment
                ShipmentDto shipmentDto = new ShipmentDto();
                BeanUtils.copyProperties(order.getShipment(), shipmentDto);
                // lấy ra payment
                PaymentDto paymentDto = new PaymentDto();
                BeanUtils.copyProperties(order.getPayment(), paymentDto);
                orderDto.setShipment(shipmentDto);
                orderDto.setPayment(paymentDto);
                // lấy ra 1 sản phẩm của đơn hàng
                CartItemEntity cartItem = order.getCartItems().get(0);
                CartItemDto cartItemDto = findOneProduct(cartItem);
                List<CartItemDto> tmp = new ArrayList<>();
                tmp.add(cartItemDto);
                orderDto.setCartItems(tmp);
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
    public JSONObject getListItemOfOrder(Long orderId) {
        try {
            JSONObject response = new JSONObject();
            OrderEntity order = orderRepository.findById(orderId).orElse(new OrderEntity());
            List<CartItemEntity> listCartItem = order.getCartItems();
            List<CartItemDto> listResponse = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(CartItemEntity cartItem : listCartItem) {
                CartItemDto cartItemDto = findOneProduct(cartItem);
                listResponse.add(cartItemDto);
            }
            response.put("listItem", listResponse);

            // lấy thông tin người mua hàng
            User user = userInfoRepository.findByIdAndDeleted(order.getUser().getId(), 0).orElse(null);
            UserDto userDto = new UserDto();
            mapper.map(user, userDto);
            userDto.setPassword(null);
            userDto.setRoles(null);
            response.put("infoUser", userDto);

            OrderDto orderDto = new OrderDto();
            mapper.map(order, orderDto);
            orderDto.setCartItems(null);
            response.put("infoOrder", orderDto);
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // đặt hàng
    @Override
    @Transactional
    public JSONObject saveOrder(OrderDto orderDto, Long shipmentMethod, Long paymentMethod, Long voucherId, String orderInfo, String responseCode, String transactionCode, String bankTranNo) {
        JSONObject response = new JSONObject();
        try {
            // status: 0-chưa thanh toán, 1-chờ xử lý, 2-đã thanh toán
            // Lấy ra method thanh toán
            PaymentEntity payment = paymentRepository.findById(paymentMethod).orElse(new PaymentEntity());
            String statusPayment;
            Date datePayment = null;
//            System.out.println("Code: " + payment.getCode());
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
            UserVoucherEntity voucher = userVoucherRepository.findById(voucherId).orElse(null);
            UserInfoUserDetails userDetails = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userInfoRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(new User());
            String statusStr = convertStatusIntToStatusStr(0);
            OrderEntity order = new OrderEntity(System.currentTimeMillis(), new Date(System.currentTimeMillis()), orderDto.getTotalPrice(),
                                null, statusPayment, orderDto.getAddress(), statusStr, datePayment, orderInfo, bankTranNo, 0, user,
                                null, payment, shipment, voucher);
            order = orderRepository.save(order);
            // lấy ra danh sách các item người dùng muốn đặt hàng
            for(Integer idCartItem : orderDto.getItemOrders()) {
                CartItemEntity cartItem = cartItemRepository.findById(idCartItem).orElse(new CartItemEntity());
                cartItem.setOrder(order);
                cartItem.setOrdered(1);
                ProductItemEntity itemDetail = cartItem.getProductItem();
                itemDetail.setQuantitySold(cartItem.getQuantity() + itemDetail.getQuantitySold());
                if(itemDetail.getQuantitySold() >= itemDetail.getQuantityInStock()) {
                    break;
                }
                cartItemRepository.save(cartItem);
                productItemRepository.save(itemDetail);
            }
            if(voucher != null) {
                voucher.setUsed(true);
                userVoucherRepository.save(voucher);
            }
            // lưu thông báo vào bảng thống báo và gửi về số lượng các thống báo chưa đọc
            String message = "Bạn có đơn hàng mới từ " + user.getName().toUpperCase();
            NotificationEntity notify = new NotificationEntity(System.currentTimeMillis(), message, 0, order.getId(), System.currentTimeMillis());
            notifyRepository.save(notify);
            // đếm số thống báo chưa đọc
            Integer countNotify = notifyRepository.countAllByAck(0);
            AlertNotify.senMessage("" + countNotify);
            BeanUtils.copyProperties(order, orderDto);
            response.put("code", 1);
            response.put("message", "Đặt hàng thành công !!");
            response.put("order", orderDto);
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Đặt hàng thất bại !!");
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
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
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

    @Override
    public JSONObject updateOrder(Integer status, Integer cancel, Long orderId) {
        JSONObject response = new JSONObject();
        try {
            OrderEntity order = orderRepository.findById(orderId).orElse(new OrderEntity());
            // TH hủy đơn hàng
            if(cancel != null) {
                Date currentDate = new Date(System.currentTimeMillis());
                Date createOrder = order.getCreateDate();
                long timeCancel = currentDate.getTime() - createOrder.getTime();
                int statusOrder = order.getStatusOrderInt();
                if(statusOrder == 2 || statusOrder == 3) {
                    response.put("code", 0);
                    response.put("message", "Đơn hàng của bạn đang được vận chuyển không thể hủy đơn hàng !!");
                    return response;
                }
                if (timeCancel >= 24*3600*1000) {
                    response.put("code", 0);
                    response.put("message", "Đơn hàng của bạn đã quá thời gian hủy đơn !!");
                    return response;
                }
                String statusStr = convertStatusIntToStatusStr(4);
                order.setStatusOrderInt(4);
                order.setStatusOrder(statusStr);
                // Gửi thông báo broker tới người phụ trách đơn hàng
                // lưu vào thông báo
                orderRepository.save(order);
            }
            // TH người phụ trách đơn hàng cập nhật xác nhận đơn hàng
            else {
                if(order.getStatusOrderInt() == 4) {
                    response.put("code", 0);
                    response.put("message", "Đơn hàng đã bị hủy, không thể thực hiện giao hàng !!");
                    return response;
                }
                String statusStr = convertStatusIntToStatusStr(status);
                order.setStatusOrder(statusStr);
                order.setStatusOrderInt(status);
                orderRepository.save(order);
            }
            response.put("code", 1);
            response.put("message", "Cập nhật đơn hàng thành công !!");
        }
        catch (Exception e) {
            e.printStackTrace();
            response.put("code", 0);
            response.put("message", "Cập nhật đơn hàng thất bại !!");
        }
        return response;
    }

    private String convertStatusIntToStatusStr(Integer status) {
        switch(status) {
            case 0:
                return "Đang xử lý";
            case 1:
                return "Đã nhận đơn";
            case 2:
                return "Đang vận chuyển";
            case 3:
                return "Đã nhận hàng";
            case 4:
                return "Đã hủy đơn";
        }
        return null;
    }

    private CartItemDto findOneProduct(CartItemEntity cartItem) {
        try {
            ModelMapper mapper = MapperUtil.configModelMapper();
            CartItemDto cartItemDto = new CartItemDto();
            mapper.map(cartItem, cartItemDto);
            Double price = Math.round(cartItem.getProductItem().getPrice() * cartItem.getQuantity() * 100.0) / 100.0;
            cartItemDto.setTotalPrice(price);
            ProductEntity product = productRepository.findByIdAndDeleted(cartItem.getProductItem().getProduct().getId(), false).orElse(new ProductEntity());
            // Lấy ra danh sách ảnh
            List<ImageEntity> images = product.getImages();
            List<ImageDto> imageDtos = new ArrayList<>();
            for(ImageEntity image : images) {
                ImageDto imageDto = new ImageDto();
                mapper.map(image, imageDto);
                imageDtos.add(imageDto);
            }
            cartItemDto.setProductId(product.getId());
            cartItemDto.setImages(imageDtos);
            JSONParser parser = new JSONParser();
            JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(cartItem.getProductItem().getId());
            JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
            JSONObject quantity = new JSONObject();
            quantity.put("quantity_stock", cartItem.getProductItem().getQuantityInStock());
            quantity.put("quantity_sold", cartItem.getProductItem().getQuantitySold());
            quantity.put("productItemId", cartItem.getProductItem().getId());
            quantity.put("price", cartItem.getProductItem().getPrice());
            productDetail.add(quantity);
            cartItemDto.setProductItemDetail(productDetail);
            cartItemDto.setProductName(product.getName());
            // Lấy ra category
            CategoryEntity category = product.getCategory();
            CategoryDto categoryDto = new CategoryDto();
            mapper.map(category, categoryDto);
            categoryDto.setVariations(null);
            cartItemDto.setCategory(categoryDto);
            return cartItemDto;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
