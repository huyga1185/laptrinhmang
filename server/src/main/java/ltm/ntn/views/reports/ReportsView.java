package ltm.ntn.views.reports;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
public class ReportsView extends JPanel {

    private final JLabel headerLabel;
    private final JTextField yearField;
    private final JButton viewButton;
    private final JButton exportButton;
    private final JLabel chartLabel;
    private final JLabel noReportLabel;

    public ReportsView() {
        setLayout(new BorderLayout(10, 10));

        // ===== Header =====
        headerLabel = new JLabel("Reports", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);

        // ===== Top input panel (year + buttons) =====
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        inputPanel.add(new JLabel("Year:"));
        yearField = new JTextField(5);
        inputPanel.add(yearField);

        viewButton = new JButton("View Report");
        inputPanel.add(viewButton);

        exportButton = new JButton("Export Report");
        inputPanel.add(exportButton);

        add(inputPanel, BorderLayout.SOUTH);

        // ===== Center panel for chart / no report =====
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartLabel = new JLabel();
        chartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chartLabel.setVerticalAlignment(SwingConstants.CENTER);

        noReportLabel = new JLabel("No report available", SwingConstants.CENTER);
        noReportLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        noReportLabel.setForeground(Color.GRAY);

        chartPanel.add(noReportLabel, BorderLayout.CENTER);
        chartPanel.add(chartLabel, BorderLayout.CENTER); // chartLabel sẽ cover noReportLabel khi có hình

        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Hiển thị ảnh PNG vào chartLabel, ẩn noReportLabel
     */
    public void showChart(BufferedImage chartImage) {
        chartLabel.setIcon(new ImageIcon(chartImage));
        noReportLabel.setVisible(false);
    }

    /**
     * Ẩn chart, hiển thị label "No report available"
     */
    public void showNoReport() {
        chartLabel.setIcon(null);
        noReportLabel.setVisible(true);
    }
}
