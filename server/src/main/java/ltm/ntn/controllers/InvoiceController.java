package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.models.pojo.Invoice;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.CouponService;
import ltm.ntn.models.services.InvoiceService;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.share.dto.requests.creations.InvoiceCreationRequest;
import ltm.ntn.share.dto.requests.creations.InvoiceItemCreationRequest;
import ltm.ntn.share.dto.requests.selections.ProductSelection;
import ltm.ntn.share.dto.responses.creations.InvoiceCreationResponse;
import ltm.ntn.share.dto.responses.creations.InvoiceItemCreationResponse;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceItemResponse;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;
import ltm.ntn.views.invoices.InvoiceAddView;
import ltm.ntn.views.invoices.InvoiceDetailView;
import ltm.ntn.views.invoices.ManageInvoicesView;
import ltm.ntn.views.utils.ErrorDialog;

import java.util.ArrayList;
import java.util.List;

public class InvoiceController {

    @Getter
    private final InvoiceAddView invoiceAddView;
    @Getter
    private final InvoiceDetailView invoiceDetailView;
    @Getter
    private final ManageInvoicesView manageInvoicesView;

    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final CouponService couponService;

    private final DashboardController dashboardController;
    private final ProductController productController;
    private final CouponController couponController;

    public InvoiceController(
        InvoiceService invoiceService,
        ProductService productService,
        CouponService couponService,
        DashboardController dashboardController,
        ProductController productController,
        CouponController couponController
    ) {
        this.invoiceAddView = new InvoiceAddView();
        this.invoiceDetailView = new InvoiceDetailView();
        this.manageInvoicesView = new ManageInvoicesView();
        this.invoiceService = invoiceService;
        this.productService = productService;
        this.couponService = couponService;

        this.dashboardController = dashboardController;
        this.productController = productController;
        this.couponController = couponController;

        initManageInvoiceView();
        initInvoiceAddView();
        initInvoiceDetailView();
    }

    private void initManageInvoiceView() {
        manageInvoicesView.getCardPanel().add(manageInvoicesView.getListPanel(), "list");
        manageInvoicesView.getCardPanel().add(invoiceAddView, "add");
        manageInvoicesView.getCardPanel().add(invoiceDetailView, "detail");

        initInvoiceList();
        showInvoiceList();

        manageInvoicesView.getBtnAddInvoice().addActionListener(e -> showInvoiceAddView());
    }

    private void showInvoiceAddView() {
        invoiceAddView.resetFullForm();
        manageInvoicesView.getCardLayout().show(manageInvoicesView.getCardPanel(), "add");
    }

    private void showInvoiceDetailView() {
        manageInvoicesView.getCardLayout().show(manageInvoicesView.getCardPanel(), "detail");
    }

    private void showInvoiceList() {
        manageInvoicesView.getCardLayout().show(manageInvoicesView.getCardPanel(), "list");
    }

    private void initInvoiceAddView() {
        initProductList();

        invoiceAddView.getBtnBack().addActionListener(e -> showInvoiceList());
        invoiceAddView.getBtnReset().addActionListener(e -> invoiceAddView.resetFullForm());
        invoiceAddView.getBtnCreate().addActionListener(e -> createInvoice());
    }

    private void initInvoiceDetailView() {
        invoiceDetailView.getBtnBack().addActionListener(e -> showInvoiceList());
    }

    private void initProductList() {
        List<Product> products = new ArrayList<>();
        try {
            products = productService.findAllProducts();
            for (Product product : products)
                invoiceAddView.addProduct(false, product.getName(), product.getPrice(), product.getQuantity(), 1, product.getId());
        } catch (Exception e) {
            ErrorDialog.showError(manageInvoicesView.getParent(), e.getMessage());
        }
    }

    private void initInvoiceList() {
        List<GetInvoiceResponse> invoices = new ArrayList<>();
        try {
            invoices = invoiceService.findAllInvoicesSafe();
            for (GetInvoiceResponse invoice : invoices)
                manageInvoicesView.addInvoice(invoice);
        } catch (Exception e) {
            ErrorDialog.showError(manageInvoicesView.getParent(), e.getMessage());
        }

        manageInvoicesView.getInvoiceList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // chỉ trigger khi chọn xong
                GetInvoiceResponse selected = manageInvoicesView.getInvoiceList().getSelectedValue();
                if (selected != null) {
                    invoiceDetailView.loadData(selected); // load dữ liệu vào view detail
                    showInvoiceDetailView(); // show detail view
                }
            }
        });
    }

    private void createInvoice() {
        System.out.println("Create invoice clicked");

        String invoiceCode = invoiceAddView.getTxtInvoiceCode().getText();
        if (invoiceCode == null || invoiceCode.isEmpty()) {
            ErrorDialog.showError(invoiceAddView.getParent(), "Please enter invoice code");
            return;
        }

        String couponCode = invoiceAddView.getTxtCouponCode().getText();
        String couponId = null; // default: không có coupon
        if (couponCode != null && !couponCode.isEmpty()) {
            try {
                Coupon c = couponService.findCouponByCouponCode(couponCode);
                if (c == null) {
                    ErrorDialog.showError(invoiceAddView.getParent(), "Coupon does not exist");
                    return;
                }
                couponId = c.getId(); // chỉ set nếu coupon tồn tại
            } catch (Exception e) {
                ErrorDialog.showError(invoiceAddView.getParent(), e.getMessage());
                return;
            }
        }

        List<ProductSelection> selected = invoiceAddView.getSelectedProducts();
        if (selected.isEmpty()) {
            ErrorDialog.showError(invoiceAddView.getParent(), "Please select at least one product");
            return;
        }

        List<InvoiceItemCreationRequest> items = new ArrayList<>();
        for (ProductSelection product : selected) {
            InvoiceItemCreationRequest item = InvoiceItemCreationRequest.builder()
                    .productId(product.getProductId())
                    .quantity(product.getQuantity())
                    .build();
            items.add(item);
        }

        InvoiceCreationRequest request = InvoiceCreationRequest.builder()
                .invoiceCode(invoiceCode)
                .couponId(couponId) // null nếu không có coupon
                .items(items)
                .build();

        try {
            InvoiceCreationResponse response = invoiceService.createInvoice(request);

            List<GetInvoiceItemResponse> its = new ArrayList<>();
            for (InvoiceItemCreationResponse item : response.getItems()) {
                GetInvoiceItemResponse it = GetInvoiceItemResponse.builder()
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build();
                its.add(it);
            }

            GetInvoiceResponse rp = GetInvoiceResponse.builder()
                    .invoiceCode(response.getInvoiceCode())
                    .discountType(response.getDiscountType())
                    .discountAmount(response.getDiscountAmount())
                    .couponCode(response.getCouponCode())
                    .totalAmount(response.getTotalAmount())
                    .createdAt(response.getCreatedAt())
                    .items(its)
                    .build();

            manageInvoicesView.addInvoice(rp);
            dashboardController.refresh();
            couponController.refresh();
            productController.refresh();
        } catch (Exception e) {
            ErrorDialog.showError(invoiceAddView.getParent(), e.getMessage());
        }
    }

    public void refresh() {
        invoiceAddView.clearAllRows();
        initProductList();
    }
}
