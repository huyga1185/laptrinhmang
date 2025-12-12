package ltm.ntn.models.dao;

import ltm.ntn.models.dao.interfaces.ICouponDAO;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.share.DBConnection;
import ltm.ntn.share.Utils;
import ltm.ntn.share.enums.DiscountType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO implements ICouponDAO {
    @Override
    public Coupon save(Connection connection, Coupon coupon) throws Exception {
        String sql;
        if (coupon.getId() == null)
            sql = "INSERT INTO coupons(id, coupon_code, discount_type, discount_amount, issue_date, expiry_date, minimum_purchase_amount, max_redemptions, redemptions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        else
            sql = "UPDATE coupons SET expiry_date = ?, max_redemptions = ? WHERE id = ?";

        try (
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            if (coupon.getId() == null) {
                coupon.setId(Utils.createUUID());
                ps.setString(1, coupon.getId());
                ps.setString(2, coupon.getCouponCode());
                ps.setString(3, coupon.getDiscountType().name().toLowerCase());
                ps.setDouble(4, coupon.getDiscountAmount());
                ps.setDate(5, Date.valueOf(coupon.getIssueDate()));
                ps.setDate(6, Date.valueOf(coupon.getExpiryDate()));
                ps.setDouble(7, coupon.getMinimumPurchaseAmount());
                ps.setInt(8, coupon.getMaxRedemptions());
                ps.setInt(9, coupon.getRedemptions());
            } else {
                ps.setDate(1, Date.valueOf(coupon.getExpiryDate()));
                ps.setInt(2, coupon.getMaxRedemptions());
                ps.setString(3, coupon.getId());
            }

            ps.executeUpdate();
        }
        return coupon;
    }

    public static Coupon mapResultSetToCoupon(ResultSet rs) throws Exception {
        Coupon coupon = new Coupon();
        coupon.setId(rs.getString("id"));
        coupon.setCouponCode(rs.getString("coupon_code"));
        coupon.setDiscountType(DiscountType.valueOf(rs.getString("discount_type").toUpperCase()));
        coupon.setDiscountAmount(rs.getDouble("discount_amount"));
        coupon.setIssueDate(rs.getDate("issue_date").toLocalDate());
        coupon.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        coupon.setMinimumPurchaseAmount(rs.getDouble("minimum_purchase_amount"));
        coupon.setMaxRedemptions(rs.getInt("max_redemptions"));
        coupon.setRedemptions(rs.getInt("redemptions"));
        return coupon;
    }

    @Override
    public Coupon getCouponById(String id) throws Exception {
        String sql = "SELECT * FROM coupons WHERE id = ?";
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToCoupon(rs);
        }
        return null;
    }

    @Override
    public List<Coupon> getAllCoupons() throws Exception {
        List<Coupon> coupons = new ArrayList<>();
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM coupons");
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Coupon coupon = mapResultSetToCoupon(rs);
                coupons.add(coupon);
            }
        }
        return coupons;
    }
    @Override
    public boolean deleteById(String id) throws Exception {
        String sql = "DELETE FROM coupons WHERE id = ?";
        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            int rows = ps.executeUpdate();
            return rows > 0; // trả true nếu xóa thành công
        }
    }

    @Override
    public Coupon getCouponByCouponCode(String code) throws Exception {
        String sql = "SELECT * FROM coupons WHERE coupon_code = ?";
        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToCoupon(rs);
        }
        return null;
    }

    @Override
    public Coupon increaseRedemptions(Connection connection, Coupon coupon) throws Exception {
        String sql = "UPDATE coupons SET redemptions = ? WHERE id = ?";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, coupon.getRedemptions());
            ps.setString(2, coupon.getId());
            ps.executeUpdate();
        }
        return coupon;
    }

}
