package ltm.ntn.views.dashboard;

import lombok.Getter;
import lombok.Setter;
import ltm.ntn.views.utils.BlockPanel;
import ltm.ntn.views.utils.ProductBlockPanel;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class DashboardView extends JPanel {

    // ---------------- Getters để thay đổi block ----------------
    private BlockPanel block1;
    private BlockPanel block2;
    private BlockPanel block3;
    private BlockPanel block4;
    private ProductBlockPanel productBlock;

    public DashboardView() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);

        // ---------------- Header ----------------
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false); // trong suốt
        JLabel titleLabel = new JLabel("Sales Activity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerPanel.add(titleLabel);

        // ---------------- Activity Panel (GridBagLayout) ----------------
        JPanel panelActivity = new JPanel(new GridBagLayout());
        panelActivity.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // khoảng cách giữa các block
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // ---------------- Blocks ----------------
        block1 = new BlockPanel("0", "Total Products Sold", Color.BLUE, 200, 150);
        block2 = new BlockPanel("0", "Total Invoices", Color.GREEN, 200, 150);
        block3 = new BlockPanel("0", "Total Coupons Used", Color.ORANGE, 200, 150);
        block4 = new BlockPanel("0", "Total Revenue", Color.BLUE, 200, 150);

        productBlock = new ProductBlockPanel(new Color(248, 248, 248, 204), 800, 450, 0, 0, 0);

        // Hàng 0
        gbc.gridx = 0; gbc.gridy = 0;
        panelActivity.add(block1, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panelActivity.add(block2, gbc);

        // Hàng 1
        gbc.gridx = 0; gbc.gridy = 1;
        panelActivity.add(block3, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panelActivity.add(block4, gbc);

        // ProductBlock nằm dưới, chiếm toàn bộ 2 cột
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // chiếm 2 cột
        panelActivity.add(productBlock, gbc);

        // ---------------- Add to Dashboard ----------------
        add(headerPanel, BorderLayout.NORTH);
        add(panelActivity, BorderLayout.CENTER);
    }
}