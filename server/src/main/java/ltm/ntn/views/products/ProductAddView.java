package ltm.ntn.views.products;

import lombok.Getter;
import javax.swing.*;
import java.awt.*;

@Getter
public class ProductAddView extends JPanel {

    private JTextField txtSku, txtName, txtDescription, txtPrice, txtQuantity;
    private JCheckBox chkActive;

    private JButton btnAdd;
    private JButton btnBack;

    public ProductAddView() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel header = new JLabel("Add New Product");
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

        // ❌ Không còn txtId
        txtSku = styledTextField();
        txtName = styledTextField();
        txtDescription = styledTextField();
        txtPrice = styledTextField();
        txtQuantity = styledTextField();
        chkActive = new JCheckBox("Active");

        chkActive.setBackground(Color.WHITE);
        chkActive.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        // ❌ Bỏ dòng addField ID ở row 0
        addField(form, gbc, 0, "SKU", txtSku);
        addField(form, gbc, 1, "Name", txtName);
        addField(form, gbc, 2, "Description", txtDescription);
        addField(form, gbc, 3, "Price", txtPrice);
        addField(form, gbc, 4, "Quantity", txtQuantity);
        addField(form, gbc, 5, "Active", chkActive);

        add(form, BorderLayout.CENTER);

        btnAdd = styledButton("➕ Thêm");
        btnBack = styledButton("⬅ Quay lại");

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnAdd);
        btnPanel.add(btnBack);

        add(btnPanel, BorderLayout.SOUTH);
    }

    // ==========================================================================

    public void resetForm() {
        txtSku.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        chkActive.setSelected(true);
    }

    public boolean isActive() {
        return chkActive.isSelected();
    }

    // ======================= UI HELPERS =====================================

    private JTextField styledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return tf;
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(new Color(60, 120, 220));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        return btn;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 0.2;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(comp, gbc);
    }
}