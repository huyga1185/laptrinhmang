package ltm.ntn.views.utils;

import ltm.ntn.models.pojo.Product;

import javax.swing.*;
import java.awt.*;

public class ProductItemRenderer extends JPanel implements ListCellRenderer<Product> {

    private JLabel lblName = new JLabel();
    private JLabel lblSku = new JLabel();
    private JLabel lblActive = new JLabel();

    public ProductItemRenderer() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSku.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        left.add(lblName);
        left.add(lblSku);

        lblActive.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblActive.setHorizontalAlignment(SwingConstants.RIGHT);

        add(left, BorderLayout.CENTER);
        add(lblActive, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list,
                                                  Product value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        lblName.setText(value.getName());
        lblSku.setText("SKU: " + value.getSku());
        lblActive.setText(value.isActive() ? "🟢 Active" : "🔴 Inactive");
        lblActive.setForeground(value.isActive() ? new Color(0, 150, 0) : Color.RED);

        if (isSelected) {
            setBackground(new Color(200, 220, 255));
        } else {
            setBackground(Color.WHITE);
        }

        setOpaque(true);
        return this;
    }
}
