package ltm.ntn.views.invoices;

import lombok.Getter;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Getter
public class InvoiceDetailView extends JPanel {

    private JLabel lblCode;
    private JLabel lblCoupon;
    private JLabel lblType;
    private JLabel lblAmount;
    private JLabel lblTotal;
    private JLabel lblCreated;

    private JTable table;
    private DefaultTableModel model;
    private JButton btnBack;

    public InvoiceDetailView() {
        setLayout(new BorderLayout());

        // ===== INFO PANEL =====
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblCode = new JLabel();
        lblCoupon = new JLabel();
        lblType = new JLabel();
        lblAmount = new JLabel();
        lblTotal = new JLabel();
        lblCreated = new JLabel();

        infoPanel.add(lblCode);
        infoPanel.add(lblCoupon);
        infoPanel.add(lblType);
        infoPanel.add(lblAmount);
        infoPanel.add(lblTotal);
        infoPanel.add(lblCreated);

        add(infoPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] cols = { "Product Name", "Quantity", "Unit Price", "Total Price" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // không cho sửa cell
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BACK BUTTON =====
        btnBack = new JButton("Back");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnBack);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Load dữ liệu invoice vào view
     */
    public void loadData(GetInvoiceResponse inv) {
        // Reset bảng
        model.setRowCount(0);

        if (inv == null) return;

        // Set label
        lblCode.setText("Invoice code: " + safe(inv.getInvoiceCode()));
        lblCoupon.setText("Coupon code: " + safe(inv.getCouponCode()));
        lblType.setText("Discount type: " + safe(inv.getDiscountType()));
        lblAmount.setText("Discount amount: " + inv.getDiscountAmount());
        lblTotal.setText("Total amount: " + inv.getTotalAmount());
        lblCreated.setText("Created at: " + (inv.getCreatedAt() != null ? inv.getCreatedAt().toString() : ""));

        // Thêm sản phẩm
        if (inv.getItems() != null) {
            inv.getItems().forEach(item -> model.addRow(new Object[]{
                    safe(item.getProductName()),
                    item.getQuantity(),
                    (int) item.getUnitPrice(),
                    (int) item.getTotalPrice()
            }));
        }

        revalidate();
        repaint();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    /**
     * Xóa tất cả dữ liệu hiện tại, đưa view về trạng thái "trống"
     */
    public void clear() {
        model.setRowCount(0);
        lblCode.setText("");
        lblCoupon.setText("");
        lblType.setText("");
        lblAmount.setText("");
        lblTotal.setText("");
        lblCreated.setText("");
    }
}
