package com.javatechie.service.impl;

import com.javatechie.dto.InvoiceDto;
import com.javatechie.dto.ProductItemInvoiceDto;
import com.javatechie.entity.InvoiceEntity;
import com.javatechie.entity.ProductItemInvoiceEntity;
import com.javatechie.repository.InvoiceRepository;
import com.javatechie.repository.ProductItemInvoiceRepository;
import com.javatechie.repository.ProductItemRepository;
import com.javatechie.service.IInvoiceService;
import com.javatechie.util.MapperUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService implements IInvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ProductItemRepository productItemRepository;
    @Override
    public List<InvoiceDto> findAllInvoice(Date start, Date end) {
        List<InvoiceDto> responses = new ArrayList<>();
        try {
            List<InvoiceEntity> listInvoice = new ArrayList<>();
            if(start == null || end == null) {
                listInvoice = invoiceRepository.findAll();
            }
            else {
                listInvoice = invoiceRepository.findAllByCreateDateBetween(start, end);
            }
            List<ProductItemInvoiceDto> listProductItemInvoices = new ArrayList<>();
            ModelMapper mapper = MapperUtil.configModelMapper();
            for(InvoiceEntity invoice : listInvoice) {
                InvoiceDto invoiceDto = new InvoiceDto();
                mapper.map(invoice, invoiceDto);
                double totalCost = 0;
                for(ProductItemInvoiceEntity productItemInvoice : invoice.getProductItemInvoices()) {
                    totalCost += productItemInvoice.getCost() * productItemInvoice.getQuantity();
                    ProductItemInvoiceDto product = new ProductItemInvoiceDto();
                    mapper.map(productItemInvoice, product);
                    // Lấy ra thông tin sản phẩm
                    JSONParser parser = new JSONParser();
                    JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(productItemInvoice.getProductItem().getId());
                    if(object != null) {
                        JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
                        JSONObject nameProduct = new JSONObject();
                        nameProduct.put("nameProduct", productItemInvoice.getProductItem().getProduct().getName());
                        productDetail.add(nameProduct);
                        product.setProduct(productDetail);
                    }
                    listProductItemInvoices.add(product);
                }
                invoiceDto.setProductItemInvoices(listProductItemInvoices);
                invoiceDto.setTotalPrice(Math.ceil(totalCost * 100.0) / 100.0);
                responses.add(invoiceDto);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responses;
    }

    @Override
    public InvoiceDto findOneById(Long id) {
        try {
            InvoiceDto invoiceDto = new InvoiceDto();
            InvoiceEntity invoice = invoiceRepository.findById(id).orElse(new InvoiceEntity());
            ModelMapper mapper = MapperUtil.configModelMapper();
            mapper.map(invoice, invoiceDto);
            double totalCost = 0;
            List<ProductItemInvoiceDto> listProductItemInvoices = new ArrayList<>();
            for(ProductItemInvoiceEntity productItemInvoice : invoice.getProductItemInvoices()) {
                totalCost += productItemInvoice.getCost() * productItemInvoice.getQuantity();
                ProductItemInvoiceDto product = new ProductItemInvoiceDto();
                mapper.map(productItemInvoice, product);
                // Lấy ra thông tin sản phẩm
                JSONParser parser = new JSONParser();
                JSONObject object = productItemRepository.findAllProductItemDetailByProductItem(productItemInvoice.getProductItem().getId());
                if(object != null) {
                    JSONArray productDetail = (JSONArray) parser.parse(object.get("item_detail").toString());
                    JSONObject nameProduct = new JSONObject();
                    nameProduct.put("nameProduct", productItemInvoice.getProductItem().getProduct().getName());
                    productDetail.add(nameProduct);
                    product.setProduct(productDetail);
                }
                listProductItemInvoices.add(product);
            }
            invoiceDto.setProductItemInvoices(listProductItemInvoices);
            invoiceDto.setTotalPrice(Math.ceil(totalCost * 100.0) / 100.0);
            return invoiceDto;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
