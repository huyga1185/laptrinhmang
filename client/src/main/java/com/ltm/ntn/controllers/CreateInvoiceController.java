package com.ltm.ntn.controllers;

import com.ltm.ntn.dto.request.InvoiceCreationRequest;
import com.ltm.ntn.dto.request.InvoiceItemRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.models.*;
import com.ltm.ntn.service.InvoiceService;
import com.ltm.ntn.service.ProductService;
import com.ltm.ntn.views.CreateInvoiceView;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Getter
public class CreateInvoiceController {
    private final CreateInvoiceView view;
    private final ProductService productService;
    private final InvoiceService invoiceService;
    private List<Product> products;
    private Map<String, Product> productMap;

    public CreateInvoiceController(ProductService productService, InvoiceService invoiceService) {
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.view = new CreateInvoiceView();
        this.products = new ArrayList<>();
        this.productMap = new HashMap<>();

        initListeners();
        loadProducts();
    }

    private void initListeners() {
        view.getBtnAddToInvoice().addActionListener(e -> addSelectedProductsToInvoice());
        view.getBtnRemoveFromInvoice().addActionListener(e -> removeSelectedFromInvoice());
        view.getBtnCreate().addActionListener(e -> createInvoice());
        view.getBtnClear().addActionListener(e -> clearForm());

        // Listen to quantity changes
        view.getSelectedTable().getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                updateTotal();
            }
        });
    }

    public void loadProducts() {
        try {
            products = productService.getAllActiveProducts();
            productMap.clear();
            for (Product p : products) {
                productMap.put(p.getId(), p);
            }
            view.loadProducts(products);
            log.info("Loaded {} products for invoice creation", products.size());
        } catch (Exception e) {
            log.error("Error loading products: ", e);
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSelectedProductsToInvoice() {
        var productTable = view.getProductTable();
        var productModel = view.getProductTableModel();
        var selectedModel = view.getSelectedTableModel();

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (int i = 0; i < productModel.getRowCount(); i++) {
            Boolean checked = (Boolean) productModel.getValueAt(i, 0);
            if (checked != null && checked) {
                String productId = (String) productModel.getValueAt(i, 1);
                String productName = (String) productModel.getValueAt(i, 2);
                String priceStr = (String) productModel.getValueAt(i, 3);

                Product product = productMap.get(productId);
                if (product != null) {
                    // Check if already in selected table
                    boolean exists = false;
                    for (int j = 0; j < selectedModel.getRowCount(); j++) {
                        if (selectedModel.getValueAt(j, 0).equals(productId)) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        double price = product.getPrice();
                        int quantity = 1;
                        double total = price * quantity;

                        selectedModel.addRow(new Object[]{
                                productId,
                                productName,
                                currencyFormat.format(price),
                                quantity,
                                currencyFormat.format(total)
                        });
                    }
                }

                // Uncheck
                productModel.setValueAt(false, i, 0);
            }
        }

        updateTotal();
    }

    private void removeSelectedFromInvoice() {
        var selectedTable = view.getSelectedTable();
        var selectedModel = view.getSelectedTableModel();

        int[] selectedRows = selectedTable.getSelectedRows();

        // Remove from bottom to top to avoid index shifting
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            selectedModel.removeRow(selectedRows[i]);
        }

        updateTotal();
    }

    private void updateTotal() {
        var selectedModel = view.getSelectedTableModel();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        double total = 0;

        for (int i = 0; i < selectedModel.getRowCount(); i++) {
            String productId = (String) selectedModel.getValueAt(i, 0);
            Product product = productMap.get(productId);

            if (product != null) {
                try {
                    int quantity = Integer.parseInt(selectedModel.getValueAt(i, 3).toString());

                    // Validate quantity
                    if (quantity > product.getQuantity()) {
                        JOptionPane.showMessageDialog(view,
                                "Số lượng vượt quá tồn kho cho sản phẩm: " + product.getName(),
                                "Cảnh báo",
                                JOptionPane.WARNING_MESSAGE);
                        quantity = product.getQuantity();
                        selectedModel.setValueAt(quantity, i, 3);
                    }

                    if (quantity < 1) {
                        quantity = 1;
                        selectedModel.setValueAt(quantity, i, 3);
                    }

                    double itemTotal = product.getPrice() * quantity;
                    selectedModel.setValueAt(currencyFormat.format(itemTotal), i, 4);
                    total += itemTotal;

                } catch (NumberFormatException e) {
                    log.error("Invalid quantity", e);
                }
            }
        }

        view.updateTotal(total);
    }

    private void createInvoice() {
        String invoiceCode = view.getTxtInvoiceCode().getText().trim();
        String couponId = view.getTxtCouponId().getText().trim();

        // Validate
        if (invoiceCode.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng nhập mã hóa đơn!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        var selectedModel = view.getSelectedTableModel();
        if (selectedModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn ít nhất một sản phẩm!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Build request
            List<InvoiceItemRequest> items = new ArrayList<>();

            for (int i = 0; i < selectedModel.getRowCount(); i++) {
                String productId = (String) selectedModel.getValueAt(i, 0);
                int quantity = Integer.parseInt(selectedModel.getValueAt(i, 3).toString());

                items.add(InvoiceItemRequest.builder()
                        .productId(productId)
                        .quantity(quantity)
                        .build());
            }

            InvoiceCreationRequest request = InvoiceCreationRequest.builder()
                    .invoiceCode(invoiceCode)
                    .couponId(couponId.isEmpty() ? null : couponId)
                    .items(items)
                    .build();

            // Call service
            Invoice createdInvoice = invoiceService.createInvoice(request);

            JOptionPane.showMessageDialog(view,
                    "Tạo hóa đơn thành công!\nMã hóa đơn: " + createdInvoice.getInvoiceCode() +
                            "\nTổng tiền: " + NumberFormat.getCurrencyInstance(new Locale("vi", "VN"))
                            .format(createdInvoice.getTotalAmount()),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

            clearForm();

        } catch (Exception e) {
            log.error("Error creating invoice: ", e);
            JOptionPane.showMessageDialog(view,
                    "Không thể tạo hóa đơn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        view.clearForm();
        updateTotal();
    }
}
