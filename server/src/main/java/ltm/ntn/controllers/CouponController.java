package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.views.coupons.CouponAddView;
import ltm.ntn.views.coupons.CouponDetailView;
import ltm.ntn.views.coupons.ManageCouponsView;

public class CouponController {
    @Getter
    private final ManageCouponsView manageCouponsView;
    @Getter
    private final CouponAddView couponAddView;
    @Getter
    private final CouponDetailView couponDetailView;

    public CouponController() {
        this.manageCouponsView = new ManageCouponsView();
        this.couponAddView = new CouponAddView(manageCouponsView);
        this.couponDetailView = new CouponDetailView(manageCouponsView);
    }
}
