package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.CouponDAO;
import ltm.ntn.models.dao.interfaces.ICouponDAO;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.models.services.interfaces.ICouponService;
import ltm.ntn.share.DBConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class CouponService implements ICouponService {
    private final ICouponDAO couponDAO;

    public CouponService() {
        couponDAO = new CouponDAO();
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) {
        try (Connection connection = DBConnection.getConnection()) {
            return couponDAO.save(connection, coupon);
        } catch (Exception e) {
            log.error("Could not create or update coupon: ", e);
            throw new RuntimeException("Could not create or update coupon");
        }
    }

    @Override
    public Coupon findCouponByID(String id) {
        try {
            return couponDAO.getCouponById(id);
        } catch (Exception e) {
            log.error("Could not find coupon by id: ", e);
            throw new RuntimeException("Could not find coupon by id");
        }
    }



    @Override
    public int deleteCoupon(String id) {
        try {
            Coupon c = findCouponByID(id);
            if (c == null) {
                log.error("Could not find coupon by id: {}", id);
                throw new RuntimeException("Could not find coupon by id: " + id);
            }
            if (c.getRedemptions() > 0) {
                c.setExpiryDate(LocalDate.now());
                c.setMaxRedemptions(c.getRedemptions());
                saveCoupon(c);
                return 0;
            } else if (c.getRedemptions() == 0) {
                couponDAO.deleteById(id);
                return 1;
            } else
                throw new RuntimeException("Could not delete coupon");
        } catch (Exception e) {
            log.error("Could not delete coupon by id: {}", id, e);
            throw new RuntimeException("Could not delete coupon by id");
        }
    }

    @Override
    public List<Coupon> findAllCoupons() {
        try {
            return couponDAO.getAllCoupons();
        } catch (Exception e) {
            log.error("Could not find all coupons: ", e);
            throw new RuntimeException("Could not find any coupons");
        }
    }

    @Override
    public Coupon findCouponByCouponCode(String code) {
        try {
            return couponDAO.getCouponByCouponCode(code);
        } catch (Exception e) {
            log.error("Could not find coupon by code: {}", code, e);
            throw new RuntimeException("Could not find coupon by code");
        }
    }

    @Override
    public int totalCouponUsed() {
        List<Coupon> coupons = findAllCoupons();
        int total = 0;
        for (Coupon coupon : coupons) {
            total += coupon.getRedemptions();
        }
        return total;
    }
}
