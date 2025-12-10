package com.ltm.ntn.views;

import lombok.Getter;
import com.ltm.ntn.models.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Getter
public class CreateInvoiceView extends JPanel {
    private JTextField txtInvoiceCode;
    private JTextField txtCouponId;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTable selectedTable;
    private DefaultTableModel selectedTableModel;
    private JButton btnAddToInvoice;
    private JButton btnRemoveFromInvoice;
    private JButton btnCreate;
    private JButton btnClear;
    private JLabel lblTotal;

    public CreateInvoiceView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("TẠO HÓA ĐƠN MỚI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(52, 73, 94));
        headerPanel.add(title, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Top: Invoice info
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Center: Split pane với 2 bảng
        JSplitPane splitPane = createSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom: Total và buttons
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
                "Thông tin hóa đơn",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        panel.add(new JLabel("Mã hóa đơn:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        txtInvoiceCode = new JTextField();
        txtInvoiceCode.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(txtInvoiceCode, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        panel.add(new JLabel("Mã Coupon (tùy chọn):"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        txtCouponId = new JTextField();
        txtCouponId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(txtCouponId, gbc);

        return panel;
    }

    private JSplitPane createSplitPane() {
        // Left: Available products
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        String[] productColumns = {"Chọn", "Mã SP", "Tên", "Giá", "Tồn kho"};
        productTableModel = new DefaultTableModel(productColumns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        productTable = new JTable(productTableModel);
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        leftPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        btnAddToInvoice = new JButton("Thêm vào hóa đơn");
        btnAddToInvoice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddToInvoice.setBackground(new Color(46, 204, 113));
        btnAddToInvoice.setForeground(Color.WHITE);
        btnAddToInvoice.setFocusPainted(false);
        leftPanel.add(btnAddToInvoice, BorderLayout.SOUTH);

        // Right: Selected products
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Sản phẩm trong hóa đơn"));

        String[] selectedColumns = {"Mã SP", "Tên", "Giá", "Số lượng", "Thành tiền"};
        selectedTableModel = new DefaultTableModel(selectedColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cho sửa số lượng
            }
        };

        selectedTable = new JTable(selectedTableModel);
        selectedTable.setRowHeight(30);
        selectedTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rightPanel.add(new JScrollPane(selectedTable), BorderLayout.CENTER);

        btnRemoveFromInvoice = new JButton("Xóa khỏi hóa đơn");
        btnRemoveFromInvoice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRemoveFromInvoice.setBackground(new Color(231, 76, 60));
        btnRemoveFromInvoice.setForeground(Color.WHITE);
        btnRemoveFromInvoice.setFocusPainted(false);
        rightPanel.add(btnRemoveFromInvoice, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5);

        return splitPane;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Total
        lblTotal = new JLabel("TỔNG TIỀN: 0 đ");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(new Color(192, 57, 43));
        panel.add(lblTotal, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnClear = new JButton("🔄 Làm mới");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnClear.setBackground(new Color(149, 165, 166));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setPreferredSize(new Dimension(120, 40));

        btnCreate = new JButton("✅ Tạo hóa đơn");
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreate.setBackground(new Color(41, 128, 185));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setPreferredSize(new Dimension(150, 40));

        buttonPanel.add(btnClear);
        buttonPanel.add(btnCreate);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    public void loadProducts(List<Product> products) {
        productTableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Product p : products) {
            if (p.isActive()) {
                productTableModel.addRow(new Object[]{
                        false, // checkbox
                        p.getId(),
                        p.getName(),
                        currencyFormat.format(p.getPrice()),
                        p.getQuantity()
                });
            }
        }
    }

    public void updateTotal(double total) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        lblTotal.setText("TỔNG TIỀN: " + currencyFormat.format(total));
    }

    public void clearForm() {
        txtInvoiceCode.setText("");
        txtCouponId.setText("");
        selectedTableModel.setRowCount(0);
        updateTotal(0);

        // Uncheck all products
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            productTableModel.setValueAt(false, i, 0);
        }
    }
}