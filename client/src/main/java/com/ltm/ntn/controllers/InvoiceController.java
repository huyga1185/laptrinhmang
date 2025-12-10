package com.ltm.ntn.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.models.Invoice;
import com.ltm.ntn.service.ExportService;
import com.ltm.ntn.service.InvoiceService;
import com.ltm.ntn.views.InvoiceDetailView;
import com.ltm.ntn.views.InvoiceListView;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class InvoiceController {
    private final InvoiceListView listView;
    private final InvoiceDetailView detailView;
    private final InvoiceService invoiceService;
    private final ExportService exportService;
    private List<Invoice> invoices;
    private Invoice currentInvoice;
    private final Runnable onBackToList;

    public InvoiceController(InvoiceService invoiceService,
                             ExportService exportService,
                             Runnable onBackToList) {
        this.invoiceService = invoiceService;
        this.exportService = exportService;
        this.onBackToList = onBackToList;
        this.listView = new InvoiceListView();
        this.detailView = new InvoiceDetailView();
        this.invoices = new ArrayList<>();

        initListeners();
        loadInvoices();
    }

    private void initListeners() {
        listView.getBtnRefresh().addActionListener(e -> loadInvoices());
        listView.getBtnViewDetail().addActionListener(e -> viewDetail());
        detailView.getBtnBack().addActionListener(e -> {
            if (onBackToList != null) {
                onBackToList.run();
            }
        });
        detailView.getBtnExportCSV().addActionListener(e -> exportToCSV());
    }

    public void loadInvoices() {
        try {
            invoices = invoiceService.getAllInvoices();
            listView.loadInvoices(invoices);
            log.info("Loaded {} invoices", invoices.size());
        } catch (Exception e) {
            log.error("Error loading invoices: ", e);
            JOptionPane.showMessageDialog(listView,
                    "Không thể tải danh sách hóa đơn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewDetail() {
        Invoice selected = listView.getSelectedInvoice(invoices);

        if (selected == null) {
            JOptionPane.showMessageDialog(listView,
                    "Vui lòng chọn một hóa đơn!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentInvoice = selected;
        detailView.displayInvoice(currentInvoice);

        // Switch view (sẽ được handle bởi MainController)
        fireShowDetailView();
    }

    private void exportToCSV() {
        if (currentInvoice == null) {
            JOptionPane.showMessageDialog(detailView,
                    "Không có hóa đơn để xuất!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu hóa đơn");
        fileChooser.setSelectedFile(new File("invoice_" + currentInvoice.getInvoiceCode() + ".csv"));

        int userSelection = fileChooser.showSaveDialog(detailView);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try {
                exportService.exportInvoiceToCSV(currentInvoice, filePath);

                JOptionPane.showMessageDialog(detailView,
                        "Xuất hóa đơn thành công!\nĐường dẫn: " + filePath,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                log.info("Exported invoice to: {}", filePath);

            } catch (Exception e) {
                log.error("Error exporting invoice: ", e);
                JOptionPane.showMessageDialog(detailView,
                        "Không thể xuất hóa đơn: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method để thông báo cho MainController chuyển view
    private void fireShowDetailView() {
        // Sẽ được xử lý trong MainController
    }

    public void showDetailView() {
        // Called by MainController when ready to show detail
    }
}
