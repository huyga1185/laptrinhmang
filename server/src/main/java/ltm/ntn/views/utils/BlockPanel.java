package ltm.ntn.views.utils;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Setter
@Getter
public class BlockPanel extends JPanel {
    private String mainNumber;
    private String bottomLabel;
    private final Color borderColor;
    private final int blockWidth;
    private final int blockHeight;

    public BlockPanel(String mainNumber, String bottomLabel, Color borderColor,
                      int blockWidth, int blockHeight) {
        this.mainNumber = mainNumber;
        this.bottomLabel = bottomLabel;
        this.borderColor = borderColor;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;

        setPreferredSize(new Dimension(blockWidth, blockHeight));
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    // Setter cho mainNumber
    public void setMainNumber(String mainNumber) {
        this.mainNumber = mainNumber;
        repaint(); // vẽ lại panel khi thay đổi
    }

    // Setter cho bottomLabel
    public void setBottomLabel(String bottomLabel) {
        this.bottomLabel = bottomLabel;
        repaint(); // vẽ lại panel khi thay đổi
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        // Bật Anti-Aliasing để text mượt và sắc nét
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, panelWidth, panelHeight);

        // Border
        g2.setColor(borderColor);
        g2.drawRect(0, 0, panelWidth - 1, panelHeight - 1);

        // --- Số to chính giữa ---
        int mainFontSize = Math.max(32, panelHeight / 3); // font tăng theo chiều cao panel
        Font bigFont = new Font("Arial", Font.BOLD, mainFontSize);
        g2.setFont(bigFont);
        FontMetrics fmBig = g2.getFontMetrics(bigFont);
        int numberWidth = fmBig.stringWidth(mainNumber);
        int numberHeight = fmBig.getAscent();
        int xNumber = (panelWidth - numberWidth) / 2;
        int yNumber = (panelHeight / 2) + (numberHeight / 2) - 8;
        g2.drawString(mainNumber, xNumber, yNumber);

        // --- Label nhỏ dưới số ---
        int labelFontSize = Math.max(12, panelHeight / 15); // font tăng theo chiều cao
        Font smallFont = new Font("Arial", Font.PLAIN, labelFontSize);
        g2.setFont(smallFont);
        FontMetrics fmSmall = g2.getFontMetrics(smallFont);
        int labelWidth = fmSmall.stringWidth(bottomLabel);
        int xLabel = (panelWidth - labelWidth) / 2;
        int yLabel = panelHeight - fmSmall.getDescent() - 5;
        g2.drawString(bottomLabel, xLabel, yLabel);
    }
}