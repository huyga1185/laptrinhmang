package com.ltm.ntn.views;

import lombok.Getter;
import com.ltm.ntn.models.Invoice;
import com.ltm.ntn.models.InvoiceItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
public class InvoiceDetailView extends JPanel {
    private JLabel lblInvoiceCode;
    private JLabel lblCouponCode;
    private JLabel lblDiscountType;
    private JLabel lblDiscountAmount;
    private JLabel lblTotalAmount;
    private JLabel lblCreatedAt;
    private JTable itemsTable;
    private DefaultTableModel itemsTableModel;
    private JButton btnBack;
    private JButton btnExportCSV;

    public InvoiceDetailView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📄 CHI TIẾT HÓA ĐƠN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(52, 73, 94));
        headerPanel.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnBack = new JButton("⬅️ Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setBackground(new Color(149, 165, 166));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnExportCSV = new JButton("📥 Xuất CSV");
        btnExportCSV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnExportCSV.setBackground(new Color(39, 174, 96));
        btnExportCSV.setForeground(Color.WHITE);
        btnExportCSV.setFocusPainted(false);
        btnExportCSV.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnBack);
        buttonPanel.add(btnExportCSV);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Info panel
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Items table
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
                "Thông tin chung",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        JLabel lbl1 = new JLabel("Mã hóa đơn:");
        lbl1.setFont(labelFont);
        panel.add(lbl1, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        lblInvoiceCode = new JLabel();
        lblInvoiceCode.setFont(valueFont);
        panel.add(lblInvoiceCode, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        JLabel lbl2 = new JLabel("Mã Coupon:");
        lbl2.setFont(labelFont);
        panel.add(lbl2, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        lblCouponCode = new JLabel();
        lblCouponCode.setFont(valueFont);
        panel.add(lblCouponCode, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        JLabel lbl3 = new JLabel("Loại giảm giá:");
        lbl3.setFont(labelFont);
        panel.add(lbl3, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        lblDiscountType = new JLabel();
        lblDiscountType.setFont(valueFont);
        panel.add(lblDiscountType, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
        JLabel lbl4 = new JLabel("Số tiền giảm:");
        lbl4.setFont(labelFont);
        panel.add(lbl4, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        lblDiscountAmount = new JLabel();
        lblDiscountAmount.setFont(valueFont);
        panel.add(lblDiscountAmount, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.2;
        JLabel lbl5 = new JLabel("Tổng tiền:");
        lbl5.setFont(labelFont);
        panel.add(lbl5, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        lblTotalAmount = new JLabel();
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalAmount.setForeground(new Color(192, 57, 43));
        panel.add(lblTotalAmount, gbc);

        // Row 5
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.2;
        JLabel lbl6 = new JLabel("Ngày tạo:");
        lbl6.setFont(labelFont);
        panel.add(lbl6, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        lblCreatedAt = new JLabel();
        lblCreatedAt.setFont(valueFont);
        panel.add(lblCreatedAt, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
                "Chi tiết sản phẩm",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(52, 73, 94)
        ));

        String[] columns = {"Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"};
        itemsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemsTable = new JTable(itemsTableModel);
        itemsTable.setRowHeight(35);
        itemsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        itemsTable.getTableHeader().setBackground(new Color(52, 73, 94));
        itemsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(itemsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void displayInvoice(Invoice invoice) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        lblInvoiceCode.setText(invoice.getInvoiceCode());
        lblCouponCode.setText(invoice.getCouponCode() != null ? invoice.getCouponCode() : "-");
        lblDiscountType.setText(invoice.getDiscountType() != null ? invoice.getDiscountType() : "-");
        lblDiscountAmount.setText(invoice.getDiscountAmount() != null ?
                currencyFormat.format(invoice.getDiscountAmount()) : "-");
        lblTotalAmount.setText(currencyFormat.format(invoice.getTotalAmount()));
        lblCreatedAt.setText(invoice.getCreatedAt().format(dateFormatter));

        // Load items
        itemsTableModel.setRowCount(0);
        for (InvoiceItem item : invoice.getItems()) {
            itemsTableModel.addRow(new Object[]{
                    item.getProductName(),
                    item.getQuantity(),
                    currencyFormat.format(item.getUnitPrice()),
                    currencyFormat.format(item.getTotalPrice())
            });
        }
    }
}