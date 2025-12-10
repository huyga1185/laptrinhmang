package com.ltm.ntn.views;

import lombok.Getter;
import com.ltm.ntn.models.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Getter
public class InvoiceListView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JButton btnViewDetail;

    public InvoiceListView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📋 DANH SÁCH HÓA ĐƠN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(52, 73, 94));
        headerPanel.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnRefresh = new JButton("🔄 Làm mới");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRefresh.setBackground(new Color(46, 204, 113));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnViewDetail = new JButton("👁️ Xem chi tiết");
        btnViewDetail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnViewDetail.setBackground(new Color(41, 128, 185));
        btnViewDetail.setForeground(Color.WHITE);
        btnViewDetail.setFocusPainted(false);
        btnViewDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnViewDetail);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Mã hóa đơn", "Mã Coupon", "Loại giảm giá", "Số tiền giảm", "Tổng tiền", "Ngày tạo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadInvoices(List<Invoice> invoices) {
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Invoice inv : invoices) {
            tableModel.addRow(new Object[]{
                    inv.getInvoiceCode(),
                    inv.getCouponCode() != null ? inv.getCouponCode() : "-",
                    inv.getDiscountType() != null ? inv.getDiscountType() : "-",
                    inv.getDiscountAmount() != null ? currencyFormat.format(inv.getDiscountAmount()) : "-",
                    currencyFormat.format(inv.getTotalAmount()),
                    inv.getCreatedAt().format(dateFormatter)
            });
        }
    }

    public Invoice getSelectedInvoice(List<Invoice> invoices) {
        int row = table.getSelectedRow();
        if (row >= 0 && row < invoices.size()) {
            return invoices.get(row);
        }
        return null;
    }
}
