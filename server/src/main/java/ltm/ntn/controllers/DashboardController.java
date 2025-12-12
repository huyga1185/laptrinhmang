package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.models.services.CouponService;
import ltm.ntn.models.services.InvoiceService;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.views.dashboard.DashboardView;
import ltm.ntn.views.utils.ProductBlockPanel;

import java.awt.*;

public class DashboardController {
    @Getter
    private final DashboardView dashboardView;
    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final CouponService couponService;

    public DashboardController(
        ProductService productService,
        InvoiceService invoiceService,
        CouponService couponService
    ) {
        dashboardView = new DashboardView();
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.couponService = couponService;
        initBlocks();
        initProductDetail();
    }

    private void initBlocks() {
        dashboardView.getBlock1().setMainNumber(String.valueOf(productService.getTotalSoldProducts()));
        dashboardView.getBlock2().setMainNumber(String.valueOf(invoiceService.getTotalInvoices()));
        dashboardView.getBlock3().setMainNumber(String.valueOf(couponService.totalCouponUsed()));
        dashboardView.getBlock4().setMainNumber(String.valueOf(invoiceService.getRevenue()));
    }

    private void initProductDetail() {
        ProductBlockPanel pbl = dashboardView.getProductBlock();
        pbl.setActiveItems(productService.countActiveProducts());
        pbl.setAllItems(productService.countProducts());
        pbl.setLowStockItems(productService.countLowStockProducts());
    }

    public void refresh() {
        dashboardView.getBlock1().setMainNumber(String.valueOf(productService.getTotalSoldProducts()));
        dashboardView.getBlock2().setMainNumber(String.valueOf(invoiceService.getTotalInvoices()));
        dashboardView.getBlock3().setMainNumber(String.valueOf(couponService.totalCouponUsed()));
        dashboardView.getBlock4().setMainNumber(String.valueOf(invoiceService.getRevenue()));
        ProductBlockPanel pbl = dashboardView.getProductBlock();
        pbl.setActiveItems(productService.countActiveProducts());
        pbl.setAllItems(productService.countProducts());
        pbl.setLowStockItems(productService.countLowStockProducts());
    }
}
