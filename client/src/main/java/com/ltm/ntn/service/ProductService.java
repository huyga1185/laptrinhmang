package com.ltm.ntn.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.models.Product;
import com.ltm.ntn.networks.TCPClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProductService {
    private final TCPClient tcpClient;
    private final Gson gson;

    public ProductService(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        this.gson = new Gson();
    }

    /**
     * Lấy danh sách tất cả sản phẩm active
     * Note: Server chưa có endpoint này, tạm thời return demo data
     */
    public List<Product> getAllActiveProducts() {
        try {
            // TODO: Khi server implement GET_ALL_PRODUCTS endpoint
            // String response = tcpClient.sendRequest("GET_ALL_PRODUCTS", "{}");
            // return gson.fromJson(response, new TypeToken<List<Product>>(){}.getType());

            // Demo data
            return createDemoProducts();

        } catch (Exception e) {
            log.error("Error getting products: ", e);
            return new ArrayList<>();
        }
    }

    private List<Product> createDemoProducts() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .id("1").name("Laptop Gaming Asus ROG")
                .sku("SKU001").description("Laptop gaming cao cấp")
                .price(25000000).quantity(10).sold(0).isActive(true)
                .build());
        products.add(Product.builder()
                .id("2").name("Chuột Logitech G502")
                .sku("SKU002").description("Chuột gaming RGB")
                .price(1200000).quantity(50).sold(0).isActive(true)
                .build());
        products.add(Product.builder()
                .id("3").name("Bàn phím Keychron K2")
                .sku("SKU003").description("Bàn phím cơ wireless")
                .price(2500000).quantity(30).sold(0).isActive(true)
                .build());
        products.add(Product.builder()
                .id("4").name("Tai nghe Sony WH-1000XM5")
                .sku("SKU004").description("Tai nghe chống ồn")
                .price(8500000).quantity(15).sold(0).isActive(true)
                .build());
        products.add(Product.builder()
                .id("5").name("Màn hình LG UltraWide")
                .sku("SKU005").description("Màn hình 34 inch")
                .price(12000000).quantity(8).sold(0).isActive(true)
                .build());
        return products;
    }
}