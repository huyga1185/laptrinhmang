package ltm.ntn.views.products;

import ltm.ntn.models.pojo.Product;

import javax.swing.*;
import java.awt.*;

public class ProductAddView extends JPanel {

    private JTextField txtId, txtSku, txtName, txtDescription, txtPrice, txtQuantity;
    private JCheckBox chkActive;
    private ManageProductsView parent;

    public ProductAddView(ManageProductsView parent) {
        this.parent = parent;

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

        txtId = styledTextField();
        txtSku = styledTextField();
        txtName = styledTextField();
        txtDescription = styledTextField();
        txtPrice = styledTextField();
        txtQuantity = styledTextField();

        chkActive = new JCheckBox("Active");
        chkActive.setBackground(Color.WHITE);
        chkActive.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addField(form, gbc, 0, "ID", txtId);
        addField(form, gbc, 1, "SKU", txtSku);
        addField(form, gbc, 2, "Name", txtName);
        addField(form, gbc, 3, "Description", txtDescription);
        addField(form, gbc, 4, "Price", txtPrice);
        addField(form, gbc, 5, "Quantity", txtQuantity);
        addField(form, gbc, 6, "Active", chkActive);

        add(form, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        JButton btnAdd = styledButton("➕ Thêm");
        JButton btnBack = styledButton("⬅ Quay lại");
        JButton btnDelete = styledButton("🗑️ Xóa");

        btnPanel.add(btnDelete); // thêm vào panel
        btnPanel.add(btnAdd);
        btnPanel.add(btnBack);
        add(btnPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProduct());
        btnBack.addActionListener(e -> parent.showListPanel());
        btnDelete.addActionListener(e -> deleteProduct());

    }

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

    /**
     * PUBLIC method used by ManageProductsView before showing the "add" panel.
     * Make sure ManageProductsView calls productAddView.resetForm() before card show.
     */
    public void resetForm() {
        txtId.setText("");
        txtSku.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        chkActive.setSelected(false);
    }

    private void addProduct() {
        try {
            Product p = new Product(
                    txtId.getText(),
                    txtName.getText(),
                    txtSku.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQuantity.getText()),
                    0,
                    chkActive.isSelected()
            );

            parent.addProduct(p);
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            parent.showListPanel();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }
    private void deleteProduct() {
        // Lấy ID sản phẩm
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
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
                boolean deleted = parent.getProductService().deleteProduct(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    parent.refreshList();
                    resetForm();
                    parent.showListPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm để xóa!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm: " + ex.getMessage());
            }
        }
    }

}
