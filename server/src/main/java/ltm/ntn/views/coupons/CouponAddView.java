package ltm.ntn.views.coupons;

import ltm.ntn.share.enums.DiscountType;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class CouponAddView extends JPanel {

    private final JTextField txtCode;
    private final JTextField txtAmount;
    private final JTextField txtMinPurchase;
    private final JTextField txtMaxRedemptions;
    private final JTextField txtExpiryDate;
    private final JTextField txtIssueDate;

    private final JComboBox<DiscountType> cbType;

    private final JButton btnAdd;
    private final JButton btnBack;

    public CouponAddView() {

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

        txtCode = field();
        txtAmount = field();
        txtMinPurchase = field();
        txtMaxRedemptions = field();
        txtExpiryDate = field();
        txtIssueDate = field();    // 👈 THÊM Ở ĐÂY

        cbType = new JComboBox<>(DiscountType.values());
        cbType.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        int row = 0;
        addField(form, gbc, row++, "Coupon Code:", txtCode);
        addField(form, gbc, row++, "Discount Type:", cbType);
        addField(form, gbc, row++, "Amount:", txtAmount);
        addField(form, gbc, row++, "Minimum Purchase:", txtMinPurchase);
        addField(form, gbc, row++, "Max Redemptions:", txtMaxRedemptions);
        addField(form, gbc, row++, "Expiry Date (YYYY-MM-DD):", txtExpiryDate);
        addField(form, gbc, row++, "Issue Date (YYYY-MM-DD):", txtIssueDate); // 👈 THÊM VÀO FORM

        add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        btns.setBackground(Color.WHITE);

        btnAdd = button("➕ Thêm");
        btnBack = button("⬅ Quay lại");

        btns.add(btnAdd);
        btns.add(btnBack);

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
        JLabel label = new JLabel(l);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(comp, gbc);
    }

    public void resetForm() {
        txtCode.setText("");
        txtAmount.setText("");
        txtMinPurchase.setText("");
        txtMaxRedemptions.setText("");
        txtExpiryDate.setText("");
        txtIssueDate.setText("");     // 👈 RESET ISSUE DATE
        cbType.setSelectedIndex(0);
    }
}