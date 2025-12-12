package ltm.ntn.views.coupons;

import ltm.ntn.models.pojo.Coupon;

import javax.swing.*;
import java.awt.*;

public class CouponDetailView extends JPanel {

    private JTextField txtExpiryDate;
    private JTextField txtMaxRedemptions;

    private JLabel lblCode, lblType, lblAmount, lblIssued;
    private ManageCouponsView parent;
    private Coupon coupon;

    public CouponDetailView(ManageCouponsView parent) {
        this.parent = parent;

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

        txtExpiryDate = textField();
        txtMaxRedemptions = textField();

        addField(form, gbc, 0, "Coupon Code:", lblCode);
        addField(form, gbc, 1, "Discount Type:", lblType);
        addField(form, gbc, 2, "Amount:", lblAmount);
        addField(form, gbc, 3, "Issued Date:", lblIssued);
        addField(form, gbc, 4, "Expiry Date:", txtExpiryDate);
        addField(form, gbc, 5, "Max Redemptions:", txtMaxRedemptions);

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton btnSave = button("💾 Lưu");
        JButton btnBack = button("⬅ Quay lại");
        JButton btnDelete = button("🗑 Xóa");

        btnPanel.add(btnSave);
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);

        btnSave.addActionListener(e -> save());
        btnBack.addActionListener(e -> parent.showListPanel());
        btnDelete.addActionListener(e -> delete());


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
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    public void setCoupon(Coupon c) {
        this.coupon = c;

        lblCode.setText(c.getCouponCode());
        lblType.setText(String.valueOf(c.getDiscountType()));
        lblAmount.setText(String.valueOf(c.getDiscountAmount()));
        lblIssued.setText(c.getIssueDate().toString());

        txtExpiryDate.setText(c.getExpiryDate().toString());
        txtMaxRedemptions.setText(String.valueOf(c.getMaxRedemptions()));
    }

    private void save() {
        coupon.setExpiryDate(java.time.LocalDate.parse(txtExpiryDate.getText()));
        coupon.setMaxRedemptions(Integer.parseInt(txtMaxRedemptions.getText()));

        JOptionPane.showMessageDialog(this, "Cập nhật coupon thành công!");
        parent.refreshList();
        parent.showListPanel();
    }
    private void delete() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa coupon này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                parent.getCouponService().deleteCoupon(coupon.getId());
                JOptionPane.showMessageDialog(this, "Đã xóa coupon!");

                parent.refreshList();
                parent.showListPanel();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Xóa thất bại: " + ex.getMessage());
            }
        }
    }

}