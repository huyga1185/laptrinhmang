package ltm.ntn.views.coupons;

import ltm.ntn.models.pojo.Coupon;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class CouponDetailView extends JPanel {

    private JTextField txtExpiryDate;
    private JTextField txtMaxRedemptions;

    private JLabel lblCode;
    private JLabel lblType;
    private JLabel lblAmount;
    private JLabel lblIssued;
    private JLabel lblRedemptions;   // 🔥 THÊM FIELD MỚI

    private JButton btnSave;
    private JButton btnBack;
    private JButton btnDelete;

    private Coupon coupon;

    public CouponDetailView() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel header = new JLabel("Coupon Details");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblCode = label();
        lblType = label();
        lblAmount = label();
        lblIssued = label();
        lblRedemptions = label();     // 🔥 label readonly

        txtExpiryDate = textField();
        txtMaxRedemptions = textField();

        int row = 0;
        addField(form, gbc, row++, "Coupon Code:", lblCode);
        addField(form, gbc, row++, "Discount Type:", lblType);
        addField(form, gbc, row++, "Amount:", lblAmount);
        addField(form, gbc, row++, "Issued Date:", lblIssued);
        addField(form, gbc, row++, "Expiry Date:", txtExpiryDate);
        addField(form, gbc, row++, "Max Redemptions:", txtMaxRedemptions);

        // 🔥 THÊM FIELD "Redemptions"
        addField(form, gbc, row++, "Redemptions:", lblRedemptions);

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        btnSave = button("💾 Lưu");
        btnDelete = button("🗑 Xóa");
        btnBack = button("⬅ Quay lại");

        btnPanel.add(btnSave);
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private JLabel label() {
        JLabel l = new JLabel();
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return l;
    }

    private JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return f;
    }

    private JButton button(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(60, 120, 220));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridy = row;

        gbc.gridx = 0;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(l, gbc);

        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    /** View hiển thị dữ liệu */
    public void setCoupon(Coupon c) {
        this.coupon = c;

        lblCode.setText(c.getCouponCode());
        lblType.setText(String.valueOf(c.getDiscountType()));
        lblAmount.setText(String.valueOf(c.getDiscountAmount()));
        lblIssued.setText(c.getIssueDate().toString());

        txtExpiryDate.setText(c.getExpiryDate().toString());
        txtMaxRedemptions.setText(String.valueOf(c.getMaxRedemptions()));

        // 🔥 FIELD MỚI
        lblRedemptions.setText(String.valueOf(c.getRedemptions()));
    }

    public void resetForm() {
        lblCode.setText("");
        lblType.setText("");
        lblAmount.setText("");
        lblIssued.setText("");
        lblRedemptions.setText("");

        txtExpiryDate.setText("");
        txtMaxRedemptions.setText("");
    }

}
