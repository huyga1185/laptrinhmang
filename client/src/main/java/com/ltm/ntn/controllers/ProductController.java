package com.ltm.ntn.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.models.Product;
import com.ltm.ntn.service.ProductService;
import com.ltm.ntn.views.ProductListView;

import javax.swing.*;
import java.util.List;

@Slf4j
@Getter
public class ProductController {
    private final ProductListView view;
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
        this.view = new ProductListView();

        initListeners();
        loadProducts();
    }

    private void initListeners() {
        view.getBtnRefresh().addActionListener(e -> loadProducts());
    }

    public void loadProducts() {
        try {
            List<Product> products = service.getAllActiveProducts();
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
