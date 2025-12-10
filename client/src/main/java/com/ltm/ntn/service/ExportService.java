package com.ltm.ntn.service;

import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.models.Invoice;
import com.ltm.ntn.models.InvoiceItem;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ExportService {

    /**
     * Xuất hóa đơn ra file CSV
     */
    public void exportInvoiceToCSV(Invoice invoice, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            // Header thông tin hóa đơn
            csvPrinter.printRecord("THÔNG TIN HÓA ĐƠN");
            csvPrinter.printRecord("Mã hóa đơn:", invoice.getInvoiceCode());

            if (invoice.getCouponCode() != null) {
                csvPrinter.printRecord("Mã coupon:", invoice.getCouponCode());
                csvPrinter.printRecord("Loại giảm giá:", invoice.getDiscountType());
                csvPrinter.printRecord("Số tiền giảm:", invoice.getDiscountAmount());
            }

            csvPrinter.printRecord("Tổng tiền:", invoice.getTotalAmount());
            csvPrinter.printRecord("Ngày tạo:",
                    invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

            csvPrinter.println();

            // Header chi tiết sản phẩm
            csvPrinter.printRecord("CHI TIẾT SẢN PHẨM");
            csvPrinter.printRecord("Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền");

            // Dữ liệu sản phẩm
            for (InvoiceItem item : invoice.getItems()) {
                csvPrinter.printRecord(
                        item.getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getTotalPrice()
                );
            }

            csvPrinter.println();
            csvPrinter.printRecord("TỔNG CỘNG:", "", "", invoice.getTotalAmount());

            log.info("Exported invoice to: {}", filePath);

        } catch (IOException e) {
            log.error("Error exporting invoice: ", e);
            throw e;
        }
    }
}
