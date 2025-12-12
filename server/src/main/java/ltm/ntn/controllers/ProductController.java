package ltm.ntn.controllers;

import lombok.Getter;
import lombok.Setter;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.views.products.ManageProductsView;
import ltm.ntn.views.products.ProductAddView;
import ltm.ntn.views.products.ProductDetailView;
import ltm.ntn.views.utils.ErrorDialog;

import javax.swing.*;
import java.util.List;

public class ProductController {
    @Getter
    private final ManageProductsView manageProductsView;
    @Getter
    private final ProductAddView productAddView;
    @Getter
    private final ProductDetailView productDetailView;

    private final ProductService productService;
    private final DashboardController dashboardController;
    @Setter
    private InvoiceController invoiceController;

    public ProductController(ProductService productService, DashboardController dashboardController) {
        this.productService = productService;
        this.manageProductsView = new ManageProductsView();
        this.productAddView = new ProductAddView();
        this.productDetailView = new ProductDetailView();
        this.dashboardController = dashboardController;

        initMangeProductsView();
        initProductAddView();
        initDetailProductView();
    }

    private void initMangeProductsView() {
        manageProductsView.getCardPanel().add(manageProductsView.getListPanel(), "list");
        initProductList();
        showListView();
        manageProductsView.getCardPanel().add(productAddView, "add");
        manageProductsView.getCardPanel().add(productDetailView, "detail");
        manageProductsView.setOnItemSelected(this::openProductDetail);

        manageProductsView.getBtnAddProduct().addActionListener(e -> showAddView());
    }
    private void initDetailProductView() {
        productDetailView.getBtnBack().addActionListener(e -> showListView());

        productDetailView.getBtnSave().addActionListener(e -> updateProduct());

        productDetailView.getBtnDelete().addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    productDetailView,
                    "Are you sure you want to delete this product?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (option == JOptionPane.YES_OPTION) {
                deleteProduct();
            }
        });
    }


    private void initProductAddView() {
        productAddView.getBtnBack().addActionListener(e -> showListView());
        productAddView.getBtnAdd().addActionListener(e -> addProduct());
    }

    private void showProductDetailView() {
        manageProductsView.getCardLayout().show(manageProductsView.getCardPanel(), "detail");
    }

    private void openProductDetail(Product p) {
        if (p == null) {
            ErrorDialog.showError(manageProductsView, "Please select a product");
            return;
        }

        productDetailView.setProduct(p);  // đổ dữ liệu vào form

        showProductDetailView();                 // chuyển card
    }

    private void showListView() {
        manageProductsView.getCardLayout().show(manageProductsView.getCardPanel(), "list");
    }

    private void showAddView() {
        productAddView.resetForm();
        manageProductsView.getCardLayout().show(manageProductsView.getCardPanel(), "add");
    }

    private void updateProduct() {
        String id = productDetailView.getLblId().getText();
        if (id == null || id.isEmpty()) {
            ErrorDialog.showError(manageProductsView, "Please select a product");
            return;
        }
        String sku = productDetailView.getLblSku().getText();
        if (sku == null || sku.isEmpty()) {
            ErrorDialog.showError(manageProductsView, "Please select a product");
            return;
        }
        String name = productDetailView.getTxtName().getText();
        if (name == null || name.isEmpty()) {
            ErrorDialog.showError(manageProductsView, "Name could not be empty");
            return;
        }
        String description = productDetailView.getTxtDescription().getText();
        double price;
        try {
            price = Double.parseDouble(productDetailView.getTxtPrice().getText());
            if (price <= 0) {
                ErrorDialog.showError(manageProductsView, "Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            ErrorDialog.showError(manageProductsView, "Price must be a number");
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(productDetailView.getTxtQuantity().getText());
            if (quantity <= 0) {
                ErrorDialog.showError(manageProductsView, "Quantity must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            ErrorDialog.showError(manageProductsView, "Quantity must be a number");
            return;
        }
        boolean isActive = productDetailView.getChkActive().isSelected();
        Product p = Product.builder()
                .id(id)
                .sku(sku)
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .isActive(isActive)
                .build();
        try {
            p = productService.saveProduct(p);
            manageProductsView.updateProduct(p);
            dashboardController.refresh();
        } catch (Exception e) {
            ErrorDialog.showError(manageProductsView, e.getMessage());
        }
    }

    private void deleteProduct() {
        String id = productDetailView.getLblId().getText();
        if (id == null || id.isEmpty()) {
            ErrorDialog.showError(manageProductsView, "Please select a product");
            return;
        }
        try {
            int m = productService.deleteProduct(id);
            if (m == 0)
                manageProductsView.removeProduct(id);
            else if (m == 1)
                manageProductsView.updateProduct(productService.findProductById(id));
            showListView();
            dashboardController.refresh();
        } catch (Exception e) {
            ErrorDialog.showError(manageProductsView, e.getMessage());
        }
    }

    private void addProduct() {
        String sku = productAddView.getTxtSku().getText();
        if (sku.isEmpty()) {
            ErrorDialog.showError(productAddView.getParent(), "SKU must not be empty");
            return;
        }
        String name = productAddView.getTxtName().getText();
        if (name.isEmpty()) {
            ErrorDialog.showError(productAddView.getParent(), "Name must not be empty");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(productAddView.getTxtPrice().getText());
            if (price <= 0) {
                ErrorDialog.showError(productAddView.getParent(), "Price must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            ErrorDialog.showError(productAddView.getParent(), "Price must be a number");
            return;
        }
        String description = productAddView.getTxtDescription().getText();
        int quantity;
        try {
            quantity = Integer.parseInt(productAddView.getTxtQuantity().getText());
            if (quantity <= 0) {
                ErrorDialog.showError(productAddView.getParent(), "Quantity must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            ErrorDialog.showError(productAddView.getParent(), "Quantity must be an integer");
            return;
        }
        boolean isActive = productAddView.isActive();
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setActive(isActive);
        try {
            Product p = productService.saveProduct(product);
            manageProductsView.addProduct(p);
            productAddView.resetForm();
            dashboardController.refresh();
            if (invoiceController != null) {
                invoiceController.refresh();
            }
        } catch (Exception e) {
            ErrorDialog.showError(productAddView.getParent(), e.getMessage());
        }
    }

    private void initProductList() {
        try {
            List<Product> ps = productService.findAllProducts();
            manageProductsView.addProducts(ps);
        } catch (Exception e) {
            ErrorDialog.showError(manageProductsView.getParent(), "Could not find any products");
        }
    }

    public void refresh() {
        initProductList();
    }
}
