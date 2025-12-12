package com.ltm.ntn.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltm.ntn.networks.TCPClient;
import com.ltm.ntn.service.ProductService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.models.Product;
import com.ltm.ntn.views.ProductListView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class ProductController {
    private final ProductListView view;
    private final Gson gson;
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
        this.gson = new Gson();
        this.view = new ProductListView();

        initListeners();
        loadProducts();
    }

    private void initListeners() {
        view.getBtnRefresh().addActionListener(e -> loadProducts());
    }

    public void loadProducts() {
        try {
            List<Product> products = productService.getAllActiveProducts();
            view.loadProducts(products);
            log.info("Loaded {} products", products.size());
        } catch (Exception e) {
            log.error("Error loading products: ", e);
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
