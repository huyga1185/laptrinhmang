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

    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final CouponService couponService;

    public HomeController(HomeView view){
        this.view = view;

        this.productService = new ProductService();
        this.invoiceService = new InvoiceService();
        this.couponService = new CouponService();

        this.dashboardController = new DashboardController(productService, invoiceService, couponService);
        this.productController = new ProductController();
        this.couponController = new CouponController();
        this.invoiceController = new InvoiceController(); // chỉ expose ManageInvoicesView

        initContentPanel();
        initSideBar();
    }

    private void initContentPanel() {
        view.getContentPanel().add(dashboardController.getDashboardView(), "Dashboard");
        view.getContentPanel().add(new ReportsView(), "Reports");
        view.getContentPanel().add(couponController.getManageCouponsView(), "ManageCoupons");
        view.getContentPanel().add(productController.getManageProductsView(), "ManageProducts");

        // Chỉ thêm ManageInvoicesView vào HomeView, không thêm invoiceAddView riêng
        view.getContentPanel().add(invoiceController.getManageInvoicesView(), "ManageInvoices");
    }

    private void initSideBar() {
        view.getDashboardButton().addActionListener(e -> view.showCard("Dashboard"));
        view.getManageProductsButton().addActionListener(e -> view.showCard("ManageProducts"));
        view.getManageCouponsButton().addActionListener(e -> view.showCard("ManageCoupons"));
        view.getManageInvoicesButton().addActionListener(e -> view.showCard("ManageInvoices"));
    }
}
