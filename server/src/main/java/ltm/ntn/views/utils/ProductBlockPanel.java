package ltm.ntn.views.utils;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public class ProductBlockPanel extends JPanel {
    private final Color borderColor;
    private final int blockWidth;
    private final int blockHeight;
    private int allItems;
    private int activeItems;
    private int lowStockItems;

    public ProductBlockPanel(
            Color borderColor,
            int blockWidth,
            int blockHeight,
            int allItems,
            int activeItems,
            int lowStockItems
    ) {
        this.borderColor = borderColor;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.allItems = allItems;
        this.activeItems = activeItems;
        this.lowStockItems = lowStockItems;

        setPreferredSize(new Dimension(blockWidth, blockHeight));
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public void setActiveItems(int activeItems) {
        this.activeItems = activeItems;
        repaint();
    }

    public void setLowStockItems(int lowStockItems) {
        this.lowStockItems = lowStockItems;
        repaint();
    }

    public void setAllItems(int allItems) {
        this.allItems = allItems;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Padding co dãn theo panelHeight để gap tăng khi fullscreen
        int paddingX = Math.max(20, panelWidth / 40);
        int paddingY = Math.max(20, panelHeight / 25);

        // Background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, panelWidth, panelHeight);

        // Border
        g2.setColor(borderColor);
        g2.drawRect(0, 0, panelWidth - 1, panelHeight - 1);

        // --- Header responsive và căn giữa ---
        String title = "PRODUCT DETAILS";
        int titleFontSize = Math.max(28, Math.min(80, panelHeight / 10)); // tăng theo height
        Font titleFont = new Font("Arial", Font.BOLD, titleFontSize);
        g2.setFont(titleFont);
        g2.setColor(Color.BLACK);
        FontMetrics titleMetrics = g2.getFontMetrics(titleFont);
        int titleWidth = titleMetrics.stringWidth(title);
        int titleHeight = titleMetrics.getAscent();
        g2.drawString(title, (panelWidth - titleWidth) / 2, paddingY + titleHeight);

        // --- Info responsive ---
        int infoFontSize = Math.max(16, Math.min(48, panelHeight / 15)); // tăng theo height
        Font infoFont = new Font("Arial", Font.PLAIN, infoFontSize);
        g2.setFont(infoFont);
        FontMetrics infoMetrics = g2.getFontMetrics(infoFont);
        int lineHeight = infoMetrics.getHeight() + infoFontSize / 2; // gap lớn hơn khi fullscreen

        int startY = paddingY + titleHeight + 2 * paddingY;

        String[] labels = {"All Items", "Active Items", "Low Stock Items"};
        int[] values = {allItems, activeItems, lowStockItems};

        // Tìm chiều rộng nhãn dài nhất
        int maxLabelWidth = 0;
        for (String label : labels) {
            int width = infoMetrics.stringWidth(label);
            if (width > maxLabelWidth) maxLabelWidth = width;
        }

        // Vẽ nhãn bên trái, số bên phải
        for (int i = 0; i < labels.length; i++) {
            int y = startY + i * lineHeight;
            g2.drawString(labels[i] + ":", paddingX, y);
            String valueStr = String.valueOf(values[i]);
            int valueWidth = infoMetrics.stringWidth(valueStr);
            g2.drawString(valueStr, panelWidth - paddingX - valueWidth, y);
        }
    }
}
