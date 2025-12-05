package ltm.ntn.views.invoices;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InvoiceDetailView extends JPanel {

    private JLabel lblCode;
    private JLabel lblCoupon;
    private JLabel lblType;
    private JLabel lblAmount;
    private JLabel lblTotal;
    private JLabel lblCreated;

    private JTable table;
    private DefaultTableModel model;
    @Getter
    private JButton btnBack;

    public InvoiceDetailView() {

        setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblCode = new JLabel("Invoice code: ");
        lblCoupon = new JLabel("Coupon code: ");
        lblType = new JLabel("Discount type: ");
        lblAmount = new JLabel("Discount amount: ");
        lblTotal = new JLabel("Total amount: ");
        lblCreated = new JLabel("Created at: ");

        infoPanel.add(lblCode);
        infoPanel.add(lblCoupon);
        infoPanel.add(lblType);
        infoPanel.add(lblAmount);
        infoPanel.add(lblTotal);
        infoPanel.add(lblCreated);

        add(infoPanel, BorderLayout.NORTH);

        String[] cols = { "Product Name", "Quantity", "Unit Price", "Total Price" };

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        add(new JScrollPane(table), BorderLayout.CENTER);

        btnBack = new JButton("Back");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);
    }
}