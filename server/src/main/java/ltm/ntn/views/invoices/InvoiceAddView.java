package ltm.ntn.views.invoices;

import lombok.Getter;
import ltm.ntn.share.dto.requests.selections.ProductSelection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class InvoiceAddView extends JPanel {

    private JTextField txtInvoiceCode;
    private JTextField txtCouponCode;
    private JTable table;
    private DefaultTableModel model;

    private JButton btnCreate;
    private JButton btnReset;
    private JButton btnBack;

    public InvoiceAddView() {

        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JLabel header = new JLabel("Create Invoice", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 24f));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // ===== TOP PANEL =====
        JPanel top = new JPanel(new GridBagLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(new JLabel("Invoice code:"), gbc);

        gbc.gridx = 1;
        txtInvoiceCode = new JTextField(12);
        top.add(txtInvoiceCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        top.add(new JLabel("Coupon code:"), gbc);

        gbc.gridx = 1;
        txtCouponCode = new JTextField(12);
        top.add(txtCouponCode, gbc);

        // ===== TABLE =====
        String[] cols = { "Select", "Product Name", "Unit Price", "Stock", "Quantity", "Product ID" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class;
                if (col == 2 || col == 3 || col == 4) return Integer.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        // HIDE PRODUCT ID
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setWidth(0);

        JScrollPane scroll = new JScrollPane(table);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        centerPanel.add(top, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.insets = new Insets(0, 10, 0, 10);
        gbcBottom.fill = GridBagConstraints.HORIZONTAL;
        gbcBottom.weightx = 1.0;

        btnCreate = new JButton("Create Invoice");
        btnReset  = new JButton("Reset");
        btnBack   = new JButton("Back to List");

        JButton[] buttons = {btnCreate, btnReset, btnBack};
        for (JButton b : buttons) {
            b.setFocusPainted(false);
            b.setBorderPainted(true);          // có viền
            b.setContentAreaFilled(false);     // phẳng
            b.setOpaque(true);                 // background vẫn nhìn thấy
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // hand cursor
        }

        gbcBottom.gridx = 0;
        bottom.add(btnCreate, gbcBottom);
        gbcBottom.gridx = 1;
        bottom.add(btnReset, gbcBottom);
        gbcBottom.gridx = 2;
        bottom.add(btnBack, gbcBottom);

        add(bottom, BorderLayout.SOUTH);

        // ===== RESET EVENT =====
        btnReset.addActionListener(e -> resetFullForm());
    }

    public void resetFullForm() {
        txtInvoiceCode.setText("");
        txtCouponCode.setText("");

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0);
            model.setValueAt(1, i, 4);
        }

        table.clearSelection();
        if (model.getRowCount() > 0)
            table.scrollRectToVisible(table.getCellRect(0, 0, true));
    }

    public void addProduct(boolean selected, String name, double unitPrice, int stock, int quantity, String productId) {
        Object[] row = { selected, name, unitPrice, stock, quantity, productId };
        model.addRow(row);
    }

    public java.util.List<ProductSelection> getSelectedProducts() {
        java.util.List<ProductSelection> list = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean selected = (boolean) model.getValueAt(i, 0);
            if (!selected) continue;

            int quantity  = (int) model.getValueAt(i, 4);
            String productId = (String) model.getValueAt(i, 5);
            list.add(new ProductSelection(productId, quantity));
        }

        return list;
    }

    public void clearAllRows() {
        model.setRowCount(0);
    }
}