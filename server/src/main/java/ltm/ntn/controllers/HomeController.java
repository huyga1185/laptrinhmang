package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.models.services.CouponService;
import ltm.ntn.models.services.InvoiceService;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.views.HomeView;
import ltm.ntn.views.reports.ReportsView;

public class HomeController {
    @Getter
    private final HomeView view;
    private final DashboardController dashboardController;
    private final ProductController productController;
    private final InvoiceController invoiceController;
    private final CouponController couponController;
    private final ReportController reportController;

    public HomeController(HomeView view){
        this.view = view;

        ProductService productService = new ProductService();
        InvoiceService invoiceService = new InvoiceService();
        CouponService couponService = new CouponService();

        this.dashboardController = new DashboardController(productService, invoiceService, couponService);
        this.productController = new ProductController(productService, dashboardController);
        this.couponController = new CouponController(couponService);
        this.invoiceController = new InvoiceController(invoiceService, productService, couponService, dashboardController, productController, couponController); // chỉ expose ManageInvoicesView
        this.reportController = new ReportController(invoiceService);

        this.productController.setInvoiceController(invoiceController);

        initContentPanel();
        initSideBar();
    }

    private void initContentPanel() {
        view.getContentPanel().add(dashboardController.getDashboardView(), "Dashboard");
        view.getContentPanel().add(reportController.getReportsView(), "Reports");
        view.getContentPanel().add(couponController.getManageCouponsView(), "ManageCoupons");
        view.getContentPanel().add(productController.getManageProductsView(), "ManageProducts");
        view.getContentPanel().add(invoiceController.getManageInvoicesView(), "ManageInvoices");
    }

    private void initSideBar() {
        view.getDashboardButton().addActionListener(e -> view.showCard("Dashboard"));
        view.getManageProductsButton().addActionListener(e -> view.showCard("ManageProducts"));
        view.getManageCouponsButton().addActionListener(e -> view.showCard("ManageCoupons"));
        view.getManageInvoicesButton().addActionListener(e -> view.showCard("ManageInvoices"));
        view.getReportsButton().addActionListener(e -> view.showCard("Reports"));
    }
}
