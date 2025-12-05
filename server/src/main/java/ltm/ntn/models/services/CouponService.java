package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.CouponDAO;
import ltm.ntn.models.dao.interfaces.ICouponDAO;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.models.services.interfaces.ICouponService;

@Slf4j
public class CouponService implements ICouponService {
    private final ICouponDAO couponDAO;

    public CouponService() {
        couponDAO = new CouponDAO();
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
    public int totalCouponUsed() {
        return 0;
    }
}
