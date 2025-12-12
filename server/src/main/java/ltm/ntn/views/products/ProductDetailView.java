package ltm.ntn.views.products;

import lombok.Getter;
import ltm.ntn.models.pojo.Product;

import javax.swing.*;
import java.awt.*;

@Getter
public class ProductDetailView extends JPanel {

    private JTextField txtName, txtDescription, txtPrice, txtQuantity;
    private JLabel lblId, lblSku;
    private JCheckBox chkActive;

    // expose buttons cho controller
    private JButton btnSave, btnBack, btnDelete;

    private Product currentProduct;

    public ProductDetailView() {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel header = new JLabel("Product Detail");
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

        lblId = styledLabel();
        lblSku = styledLabel();
        txtName = styledTextField();
        txtDescription = styledTextField();
        txtPrice = styledTextField();
        txtQuantity = styledTextField();
        chkActive = new JCheckBox("Active");
        chkActive.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chkActive.setBackground(Color.WHITE);

        addField(form, gbc, 0, "ID", lblId);
        addField(form, gbc, 1, "SKU", lblSku);
        addField(form, gbc, 2, "Name", txtName);
        addField(form, gbc, 3, "Description", txtDescription);
        addField(form, gbc, 4, "Price", txtPrice);
        addField(form, gbc, 5, "Quantity", txtQuantity);
        addField(form, gbc, 6, "Active", chkActive);

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        btnSave = styledButton("💾 Lưu");
        btnBack = styledButton("⬅ Quay lại");
        btnDelete = styledButton("🗑️ Xóa");
        btnDelete.setBackground(new Color(220, 60, 60));

        btnPanel.add(btnDelete);
        btnPanel.add(btnSave);
        btnPanel.add(btnBack);

        add(btnPanel, BorderLayout.SOUTH);
    }

    // =========================== PUBLIC API ==============================

    public void setProduct(Product p) {
        currentProduct = p;
        lblId.setText(p.getId());
        lblSku.setText(p.getSku());
        txtName.setText(p.getName());
        txtDescription.setText(p.getDescription());
        txtPrice.setText(String.valueOf(p.getPrice()));
        txtQuantity.setText(String.valueOf(p.getQuantity()));
        chkActive.setSelected(p.isActive());
    }

    public void updateProductFromFields() {
        currentProduct.setName(txtName.getText());
        currentProduct.setDescription(txtDescription.getText());
        currentProduct.setPrice(Double.parseDouble(txtPrice.getText()));
        currentProduct.setQuantity(Integer.parseInt(txtQuantity.getText()));
        currentProduct.setActive(chkActive.isSelected());
    }

    // =========================== UI HELPERS ==============================

    private JLabel styledLabel() {
        JLabel lbl = new JLabel();
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return lbl;
    }

    private JTextField styledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
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
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
