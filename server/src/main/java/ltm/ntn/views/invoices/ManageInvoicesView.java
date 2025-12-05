package ltm.ntn.views.invoices;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageInvoicesView extends JPanel {

    @Getter
    private JTable table;
    @Getter
    private DefaultTableModel model;

    public JButton btnAddInvoice; // <-- PUBLIC để controller sử dụng

    public ManageInvoicesView() {

        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Manage Invoices");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerPanel.add(title, BorderLayout.WEST);

        // ===== ADD BUTTON =====
        btnAddInvoice = new JButton("➕ Add Invoice");
        btnAddInvoice.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnAddInvoice.setBackground(new Color(60, 120, 200));
        btnAddInvoice.setForeground(Color.WHITE);
        btnAddInvoice.setFocusPainted(false);
        btnAddInvoice.setPreferredSize(new Dimension(160, 40));

        headerPanel.add(btnAddInvoice, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] cols = {
                "Invoice Code",
                "Coupon Code",
                "Discount Type",
                "Discount Amount",
                "Total Amount",
                "Created At",
                "View"
        };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6; // chỉ cột View có thể click (button)
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        // Renderer cho cột VIEW
        table.getColumnModel().getColumn(6).setCellRenderer((tbl, val, row, col, sel, focus) -> {
            JButton btn = new JButton("View");
            btn.setFocusPainted(false);
            return btn;
        });

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }
}