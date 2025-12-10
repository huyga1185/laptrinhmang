package ltm.ntn.views.invoices;

import lombok.Getter;
import ltm.ntn.share.dto.requests.selections.ProductSelection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

@Getter
public class InvoiceAddView extends JPanel {

    private JTextField txtCouponCode;
    private JTable table;
    private DefaultTableModel model;

    private JButton btnCreate;
    private JButton btnReset;
    private JButton btnBack;

    public InvoiceAddView() {

        setLayout(new BorderLayout());

        // ===== TOP PANEL =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Coupon code:"));
        txtCouponCode = new JTextField(20);
        top.add(txtCouponCode);
        add(top, BorderLayout.NORTH);

        // ===== TABLE COLUMNS =====
        String[] cols = {
                "Select",
                "Product Name",
                "Unit Price",
                "Stock",
                "Quantity",
                "Product ID"
        };

        // ===== MODEL =====
        model = new DefaultTableModel(cols, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class;
                if (col == 2 || col == 3 || col == 4) return Integer.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 4; // chỉ checkbox + quantity
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        // ===== DISABLE RESIZE + REORDER =====
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        // ===== HIDE PRODUCT ID =====
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setWidth(0);

        JScrollPane scroll = new JScrollPane(table);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scroll, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnCreate = new JButton("Create Invoice");
        btnReset  = new JButton("Reset");
        btnBack   = new JButton("Back to List");

        bottom.add(btnCreate);
        bottom.add(btnReset);
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);

        // ===== RESET EVENT =====
        btnReset.addActionListener(e -> resetFullForm());
    }

    public void resetFullForm() {
        txtCouponCode.setText("");

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0); // uncheck
            model.setValueAt(1, i, 4);     // quantity = 1
        }

        table.clearSelection();
        if (model.getRowCount() > 0)
            table.scrollRectToVisible(table.getCellRect(0, 0, true));
    }

    public void addProduct(
            boolean selected,
            String name,
            int unitPrice,
            int stock,
            int quantity,
            int productId)
    {
        Object[] row = { selected, name, unitPrice, stock, quantity, productId };
        model.addRow(row);
    }

    public java.util.List<ProductSelection> getSelectedProducts() {
        java.util.List<ProductSelection> list = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean selected = (boolean) model.getValueAt(i, 0);
            if (!selected) continue;

            int quantity  = (int) model.getValueAt(i, 4);
            int productId = (int) model.getValueAt(i, 5);
            list.add(new ProductSelection(productId, quantity));
        }

        return list;
    }

    public void clearAllRows() {
        model.setRowCount(0);
    }
}
