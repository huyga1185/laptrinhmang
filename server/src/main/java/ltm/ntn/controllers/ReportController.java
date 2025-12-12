package ltm.ntn.controllers;

import lombok.Getter;
import ltm.ntn.models.services.InvoiceService;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.views.reports.ReportsView;
import ltm.ntn.views.utils.ErrorDialog;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;

public class ReportController {
    @Getter
    private final ReportsView reportsView;
    private final InvoiceService invoiceService;

    public ReportController(InvoiceService invoiceService) {
        reportsView = new ReportsView();
        this.invoiceService = invoiceService;
        reportsView.showNoReport();
        reportsView.getViewButton().addActionListener(e -> showChart());
        reportsView.getExportButton().addActionListener(e -> {
            // 1. Tạo chart
            int year;
            try {
                year = Integer.parseInt(reportsView.getYearField().getText());
            } catch (Exception ex) {
                ErrorDialog.showError(reportsView.getParent(), "Year must be an integer");
                return;
            }

            BufferedImage chart = createChartImage(invoiceService.getRevenueByMonth(year), 500, 500);

            // 2. Mở hộp thoại Save
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save PDF");
            fileChooser.setSelectedFile(new java.io.File("MonthlyRevenue.pdf")); // tên mặc định
            int userSelection = fileChooser.showSaveDialog(reportsView);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                exportChartToPDF(chart, fileToSave.getAbsolutePath());
            }
        });

    }

    private void showChart() {
        int year = -1;
        try {
            year = Integer.parseInt(reportsView.getYearField().getText());
        } catch (Exception e) {
            ErrorDialog.showError(reportsView.getParent(), "Year must be an integer");
        }
        if (year == -1)
            ErrorDialog.showError(reportsView.getParent(), "Year must be an integer");
        Hashtable<String, Double> map = invoiceService.getRevenueByMonth(year);
        reportsView.showChart(createChartImage(map, 500, 500));
    }

    private BufferedImage createChartImage(Hashtable<String, Double> revenueByMonth, int width, int height) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Thêm tháng 01 → 12 theo thứ tự
        for (int m = 1; m <= 12; m++) {
            String monthKey = String.format("%02d", m); // "01", "02", ..., "12"
            double revenue = revenueByMonth.getOrDefault(monthKey, 0.0);
            dataset.addValue(revenue, "Revenue", monthKey);
        }

        // Tạo bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Monthly Revenue",        // chart title
                "Month",                  // domain axis label
                "Revenue",                // range axis label
                dataset,                  // dataset
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips
                false                     // URLs
        );

        // Bật anti-alias nếu muốn đẹp hơn
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        // Xuất ra BufferedImage
        return chart.createBufferedImage(width, height);
    }

    public void exportChartToPDF(BufferedImage chartImage, String pdfPath) {
        try {
            if (chartImage == null) {
                ErrorDialog.showError(reportsView.getParent(), "No chart to export");
                return;
            }

            // 1. Tạo Document PDF
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(pdfPath)));
            document.open();

            // 2. Chuyển BufferedImage thành iText Image
            Image pdfImage = Image.getInstance(chartImage, null);

            // 3. Scale hình vừa với trang PDF
            float pageWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
            float pageHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
            pdfImage.scaleToFit(pageWidth, pageHeight);

            // 4. Căn giữa
            pdfImage.setAlignment(Image.ALIGN_CENTER);

            // 5. Thêm vào PDF
            document.add(pdfImage);

            document.close();
            JOptionPane.showMessageDialog(reportsView, "PDF exported successfully to:\n" + pdfPath);

        } catch (Exception e) {
            ErrorDialog.showError(reportsView.getParent(), "Failed to export PDF: " + e.getMessage());
        }
    }

}
