package ltm.ntn.views.products;

import ltm.ntn.models.pojo.Product;

import javax.swing.*;
import java.awt.*;

public class ProductDetailView extends JPanel {

    private JTextField txtName, txtDescription, txtPrice, txtQuantity;
    private JLabel lblId, lblSku;
    private JCheckBox chkActive;
    private Product currentProduct;
    private ManageProductsView parent;

    public ProductDetailView(ManageProductsView parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel header = new JLabel("Product Detail");
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // Form
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

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton btnSave = styledButton("💾 Lưu");
        JButton btnBack = styledButton("⬅ Quay lại");
        JButton btnDelete = styledButton("🗑️ Xóa");
        btnDelete.setBackground(new Color(220, 60, 60)); // màu đỏ

        btnPanel.add(btnDelete);
        btnPanel.add(btnSave);
        btnPanel.add(btnBack);
        add(btnPanel, BorderLayout.SOUTH);

        // Action listeners
        btnSave.addActionListener(e -> saveProduct());
        btnBack.addActionListener(e -> {
            if (isModified()) {
                int opt = JOptionPane.showConfirmDialog(
                        this, "Bạn có muốn lưu thay đổi?", "Chưa lưu",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );

                if (opt == JOptionPane.YES_OPTION) {
                    saveProduct();
                    parent.showListPanel();
                } else if (opt == JOptionPane.NO_OPTION) {
                    parent.showListPanel();
                }
            } else {
                parent.showListPanel();
            }
        });
        btnDelete.addActionListener(e -> deleteProduct());
    }

    // Nút Xóa
    private void deleteProduct() {
        if (currentProduct == null || currentProduct.getId() == null) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa sản phẩm này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = parent.getProductService().deleteProduct(currentProduct.getId());
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    parent.refreshList();
                    parent.showListPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm để xóa!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm: " + ex.getMessage());
            }
        }
    }

    // Nút Lưu
    private void saveProduct() {
        if (currentProduct == null) return;

        try {
            currentProduct.setName(txtName.getText());
            currentProduct.setDescription(txtDescription.getText());
            currentProduct.setPrice(Double.parseDouble(txtPrice.getText()));
            currentProduct.setQuantity(Integer.parseInt(txtQuantity.getText()));
            currentProduct.setActive(chkActive.isSelected());

            // Lưu vào database thông qua service
            parent.getProductService().saveProduct(currentProduct);

            JOptionPane.showMessageDialog(this, "Lưu sản phẩm thành công!");
            parent.refreshList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ: " + ex.getMessage());
        }
    }

    // Hiển thị thông tin sản phẩm
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


    // Kiểm tra có thay đổi so với dữ liệu gốc
    private boolean isModified() {
        if (currentProduct == null) return false;
        if (!currentProduct.getName().equals(txtName.getText())) return true;
        if (!currentProduct.getDescription().equals(txtDescription.getText())) return true;
        if (currentProduct.getPrice() != parseDouble(txtPrice.getText())) return true;
        if (currentProduct.getQuantity() != parseInt(txtQuantity.getText())) return true;
        if (currentProduct.isActive() != chkActive.isSelected()) return true;
        return false;
    }

    // =================== Helper Methods ===================
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

    private double parseDouble(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return -99999; }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return -99999; }
    }
}
