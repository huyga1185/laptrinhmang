package ltm.ntn.views.utils;

import ltm.ntn.models.pojo.Coupon;

import javax.swing.*;
import java.awt.*;

public class CouponItemRenderer extends JPanel implements ListCellRenderer<Coupon> {

    private JLabel lblCode = new JLabel();
    private JLabel lblType = new JLabel();
    private JLabel lblStatus = new JLabel();

    public CouponItemRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        lblCode.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));

        left.add(lblCode);
        left.add(lblType);

        add(left, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Coupon> list,
                                                  Coupon coupon,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        lblCode.setText("Mã: " + coupon.getCouponCode());
        lblType.setText("Loại: " + coupon.getDiscountType() + " - " + coupon.getDiscountAmount());

        boolean isExpired = coupon.getExpiryDate().isBefore(java.time.LocalDate.now());
        lblStatus.setText(isExpired ? "🔴 Expired" : "🟢 Active");
        lblStatus.setForeground(isExpired ? Color.RED : new Color(0, 150, 0));

        setBackground(isSelected ? new Color(200, 220, 255) : Color.WHITE);
        setOpaque(true);
        return this;
    }
}