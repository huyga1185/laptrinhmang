package ltm.ntn.models.services.interfaces;

import ltm.ntn.models.pojo.Coupon;

import java.sql.Connection;
import java.util.List;

public interface ICouponService {
    Coupon saveCoupon(Coupon coupon);
    Coupon findCouponByID(String id);
    int totalCouponUsed();
    int deleteCoupon(String id);
    List<Coupon> findAllCoupons();
    Coupon findCouponByCouponCode(String code);
}

