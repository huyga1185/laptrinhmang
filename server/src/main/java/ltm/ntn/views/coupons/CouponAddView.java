package ltm.ntn.views.coupons;


import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.share.enums.DiscountType;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class CouponAddView extends JPanel {

    private JTextField txtId, txtCode, txtAmount, txtMinPurchase, txtMaxRedemptions, txtExpiryDate;
    private JComboBox<DiscountType> cbType;
    private ManageCouponsView parent;

    public CouponAddView(ManageCouponsView parent) {
        this.parent = parent;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel header = new JLabel("Add New Coupon");
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = field();
        txtCode = field();
        txtAmount = field();
        txtMinPurchase = field();
        txtMaxRedemptions = field();
        txtExpiryDate = field();

        cbType = new JComboBox<>(DiscountType.values());
        cbType.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addField(form, gbc, 0, "ID:", txtId);
        addField(form, gbc, 1, "Coupon Code:", txtCode);
        addField(form, gbc, 2, "Discount Type:", cbType);
        addField(form, gbc, 3, "Amount:", txtAmount);
        addField(form, gbc, 4, "Minimum Purchase:", txtMinPurchase);
        addField(form, gbc, 5, "Max Redemptions:", txtMaxRedemptions);
        addField(form, gbc, 6, "Expiry Date (YYYY-MM-DD):", txtExpiryDate);

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        JButton btnAdd = button("➕ Thêm");
        JButton btnBack = button("⬅ Quay lại");

        btns.add(btnAdd);
        btns.add(btnBack);

        btnAdd.addActionListener(e -> add());
        btnBack.addActionListener(e -> parent.showListPanel());

        add(btns, BorderLayout.SOUTH);
    }

    private JTextField field() {
        JTextField t = new JTextField();
        t.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return t;
    }

    private JButton button(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(60,120,200));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String l, Component comp) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(l), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    public void resetForm() {
        txtId.setText("");
        txtCode.setText("");
        txtAmount.setText("");
        txtMinPurchase.setText("");
        txtMaxRedemptions.setText("");
        txtExpiryDate.setText("");
        cbType.setSelectedIndex(0);
    }

    private void add() {
        try {
            Coupon c = new Coupon(
                    txtId.getText(),
                    txtCode.getText(),
                    (DiscountType) cbType.getSelectedItem(),
                    Double.parseDouble(txtAmount.getText()),
                    LocalDate.now(),
                    LocalDate.parse(txtExpiryDate.getText()),
                    Double.parseDouble(txtMinPurchase.getText()),
                    Integer.parseInt(txtMaxRedemptions.getText()),
                    0
            );

            parent.addCoupon(c);
            JOptionPane.showMessageDialog(this, "Thêm coupon thành công!");
            parent.showListPanel();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!");
        }
    }
}