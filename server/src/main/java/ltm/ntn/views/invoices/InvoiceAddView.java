package ltm.ntn.views.invoices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InvoiceAddView extends JPanel {

    private JTextField txtCouponCode;
    private JTable table;
    private DefaultTableModel model;

    private JButton btnAddRow;
    private JButton btnCreate;
    private JButton btnReset;
    private JButton btnBack;

    public InvoiceAddView() {

        setLayout(new BorderLayout());

        // ===== TOP PANEL: COUPON CODE =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Coupon code:"));
        txtCouponCode = new JTextField(20);
        top.add(txtCouponCode);
        add(top, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] cols = {
                "Select",
                "Product Name",
                "Unit Price",
                "Stock",
                "Quantity"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;   // checkbox
                if (columnIndex == 4) return Integer.class;   // quantity
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 4; // chỉ checkbox + quantity được sửa
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(table);

        // ===== ADD PRODUCT BUTTON =====
        JPanel midButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddRow = new JButton("➕ Add Product");
        midButtons.add(btnAddRow);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scroll, BorderLayout.CENTER);
        centerPanel.add(midButtons, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnCreate = new JButton("Create Invoice");
        btnReset = new JButton("Reset");
        btnBack = new JButton("Back to List");

        bottom.add(btnCreate);
        bottom.add(btnReset);
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);

        // ===== EVENT: ADD ROW =====
        btnAddRow.addActionListener(e -> {
            Object[] row = { false, "", "", "", 0 };
            model.addRow(row);
        });

        // ===== EVENT: RESET =====
        btnReset.addActionListener(e -> {
            txtCouponCode.setText("");
            model.setRowCount(0);
        });
    }
}