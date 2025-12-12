package ltm.ntn.models.dao.interfaces;

import ltm.ntn.models.pojo.Coupon;

import java.sql.Connection;
import java.util.List;

public interface ICouponDAO {
    Coupon save(Connection connection, Coupon coupon) throws Exception;
    Coupon getCouponById(String id) throws Exception;
    List<Coupon> getAllCoupons() throws Exception;
    boolean deleteById(String id) throws Exception;

}
