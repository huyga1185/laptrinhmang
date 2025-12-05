package ltm.ntn.models.services.interfaces;

import ltm.ntn.models.pojo.Coupon;

public interface ICouponService {
    Coupon findCouponByID(String id);
    int totalCouponUsed();
}
