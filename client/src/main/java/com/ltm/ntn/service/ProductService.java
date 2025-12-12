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

    public ProductService(TCPClient tcpClient, Gson gson) {
        this.tcpClient = tcpClient;
        this.gson = gson;
    }

    /**
     * Lấy danh sách tất cả sản phẩm active
     * Note: Server chưa có endpoint này, tạm thời return demo data
     */
    public List<Product> getAllActiveProducts() {
        try {
            // TODO: Khi server implement GET_ALL_PRODUCTS endpoint
             String response = tcpClient.sendRequest("GET_ALL_PRODUCTS", "{}");
             return gson.fromJson(response, new TypeToken<List<Product>>(){}.getType());

        } catch (Exception e) {
            log.error("Error getting products: ", e);
            return new ArrayList<>();
        }
    }

}