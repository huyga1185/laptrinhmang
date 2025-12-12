package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.models.services.CouponService;
import ltm.ntn.share.enums.DiscountType;
import ltm.ntn.views.coupons.CouponAddView;
import ltm.ntn.views.coupons.CouponDetailView;
import ltm.ntn.views.coupons.ManageCouponsView;
import ltm.ntn.views.utils.ErrorDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CouponController {
    @Getter
    private final ManageCouponsView manageCouponsView;
    @Getter
    private final CouponAddView couponAddView;
    @Getter
    private final CouponDetailView couponDetailView;

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
        this.manageCouponsView = new ManageCouponsView();
        this.couponAddView = new CouponAddView();
        this.couponDetailView = new CouponDetailView();

        initManageCouponsView();
        initCouponAddView();
        initCouponDetailView();
    }

    private void initManageCouponsView() {
        manageCouponsView.getCardPanel().add(manageCouponsView.getListPanel(), "list");
        initCouponList();
        showCouponList();
        manageCouponsView.getCardPanel().add(couponAddView, "add");
        manageCouponsView.getCardPanel().add(couponDetailView, "detail");

        manageCouponsView.getBtnAdd().addActionListener(e -> showAddView());
        manageCouponsView.setOnItemSelected(this::openCouponDetail);
    }

    private void initCouponAddView() {
        couponAddView.getBtnAdd().addActionListener(e -> addCoupon());
        couponAddView.getBtnBack().addActionListener(e -> showCouponList());
    }

    private void initCouponDetailView() {
        couponDetailView.getBtnBack().addActionListener(e -> showCouponList());
        couponDetailView.getBtnSave().addActionListener(e -> updateCoupon());
        couponDetailView.getBtnDelete().addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                    couponDetailView,
                    "Are you sure you want to delete this coupon?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (option == JOptionPane.YES_OPTION) {
                deleteCoupon();
            }
        });
    }

    private void showCouponList() {
        manageCouponsView.getCardLayout().show(manageCouponsView.getCardPanel(), "list");
    }

    private void showCouponDetailView() {
        manageCouponsView.getCardLayout().show(manageCouponsView.getCardPanel(), "detail");
    }

    private void openCouponDetail(Coupon coupon) {
        if (coupon == null) {
            ErrorDialog.showError(manageCouponsView.getCardPanel(), "Please select a coupon");
            return;
        }

        couponDetailView.resetForm();
        couponDetailView.setCoupon(coupon);
        showCouponDetailView();
    }

    private void showAddView() {
        couponAddView.resetForm();
        manageCouponsView.getCardLayout().show(manageCouponsView.getCardPanel(), "add");
    }

    private void initCouponList() {
        try {
            List<Coupon> cs = couponService.findAllCoupons();
            manageCouponsView.setCoupons(cs);
        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), e.getMessage());
        }
    }

    private void addCoupon() {
        String couponCode = couponAddView.getTxtCode().getText();
        double amount, minPurchase;
        int maxRedemption;

        try {
            amount = Double.parseDouble(couponAddView.getTxtAmount().getText());
            if  (amount <= 0) {
                ErrorDialog.showError(manageCouponsView.getParent(), "Invalid amount");
                return;
            }

            minPurchase = Double.parseDouble(couponAddView.getTxtMinPurchase().getText());
            if (minPurchase < 0) {
                ErrorDialog.showError(manageCouponsView.getParent(), "min purchase must be >= 0");
                return;
            }

            maxRedemption = Integer.parseInt(couponAddView.getTxtMaxRedemptions().getText());
            if (maxRedemption == 0 || maxRedemption < -1) {
                ErrorDialog.showError(manageCouponsView.getParent(),
                        "Max redemptions must be -1 (unlimited) or a positive number");
                return;
            }
        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(),
                    "Amount, min purchase, max redemption must be a number");
            return;
        }

        DiscountType dt;
        try {
            String type = Objects.requireNonNull(
                    couponAddView.getCbType().getSelectedItem()).toString().toUpperCase();

            if (type.equals("PERCENTAGE"))
                dt = DiscountType.PERCENTAGE;
            else if (type.equals("FIXED_AMOUNT"))
                dt = DiscountType.FIXED_AMOUNT;
            else {
                ErrorDialog.showError(manageCouponsView.getParent(), "Invalid discount type");
                return;
            }
        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Invalid type");
            return;
        }

        // 🔥 Validate percentage amount
        if (dt == DiscountType.PERCENTAGE) {
            if (amount > 100) {
                ErrorDialog.showError(manageCouponsView.getParent(), "Percentage discount must be > 0 and <= 100");
                return;
            }
        }

        LocalDate issueDate, expiryDate;
        try {
            issueDate = LocalDate.parse(couponAddView.getTxtIssueDate().getText());
            expiryDate = LocalDate.parse(couponAddView.getTxtExpiryDate().getText());

            if (issueDate.isBefore(LocalDate.now())) {
                ErrorDialog.showError(manageCouponsView.getParent(), "Issue date cannot be in the past");
                return;
            }

            if (issueDate.isAfter(expiryDate)) {
                ErrorDialog.showError(manageCouponsView.getParent(),
                        "Issue date cannot be after expiry date!");
                return;
            }

        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Invalid issueDate");
            return;
        }

        Coupon c = Coupon.builder()
                .couponCode(couponCode)
                .discountType(dt)
                .discountAmount(amount)
                .minimumPurchaseAmount(minPurchase)
                .maxRedemptions(maxRedemption)
                .issueDate(issueDate)
                .expiryDate(expiryDate)
                .build();

        try {
            c = couponService.saveCoupon(c);
            manageCouponsView.addCouponToList(c);
        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), e.getMessage());
        }
    }

    private void updateCoupon() {
        String couponCode = couponDetailView.getLblCode().getText();
        if (couponCode == null || couponCode.isEmpty()) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Coupon code cannot be empty.");
            return;
        }

        Coupon c = couponService.findCouponByCouponCode(couponCode);
        if (c == null) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Coupon not found.");
            return;
        }

        // ===== EXPIRY DATE =====
        LocalDate expiryDate;
        try {
            expiryDate = LocalDate.parse(couponDetailView.getTxtExpiryDate().getText());

            if (expiryDate.isBefore(LocalDate.now())) {
                ErrorDialog.showError(manageCouponsView.getParent(), "Expiry date cannot be in the past.");
                return;
            }

        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Invalid expiry date format. Use yyyy-MM-dd.");
            return;
        }

        c.setExpiryDate(expiryDate);

        // ===== MAX REDEMPTIONS =====
        int maxRedemption;
        try {
            maxRedemption = Integer.parseInt(couponDetailView.getTxtMaxRedemptions().getText());

            if (maxRedemption == 0 || maxRedemption < -1) {
                ErrorDialog.showError(manageCouponsView.getParent(),
                        "Max redemptions must be -1 (unlimited) or a positive number.");
                return;
            }

            if (maxRedemption != -1 && maxRedemption < c.getRedemptions()) {
                ErrorDialog.showError(manageCouponsView.getParent(),
                        "Max redemptions cannot be smaller than the current redemption count.");
                return;
            }

        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Max redemptions must be a valid number.");
            return;
        }

        c.setMaxRedemptions(maxRedemption);

        // ===== SAVE =====
        try {
            Coupon nc = couponService.saveCoupon(c);
            manageCouponsView.updateCouponInList(nc);
        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Failed to update coupon: " + e.getMessage());
        }
    }
    private void deleteCoupon() {
        String couponCode = couponDetailView.getLblCode().getText();
        if (couponCode == null || couponCode.isEmpty()) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Coupon code cannot be empty.");
            return;
        }

        Coupon c = couponService.findCouponByCouponCode(couponCode);
        if (c == null) {
            ErrorDialog.showError(manageCouponsView.getParent(), "Coupon not found.");
            return;
        }

        try {
            int mc = couponService.deleteCoupon(c.getId());
            if (mc == 0)
                manageCouponsView.updateCouponInList(c);
            else if (mc == 1)
                manageCouponsView.removeCoupon(c.getId());
            showCouponList();
        } catch (Exception e) {
            ErrorDialog.showError(manageCouponsView.getParent(), e.getMessage());
        }
    }
    public void refresh() {
        initCouponList();
    }
}
