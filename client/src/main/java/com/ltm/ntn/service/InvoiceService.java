package com.ltm.ntn.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltm.ntn.models.Invoice;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.dto.request.InvoiceCreationRequest;
import com.ltm.ntn.networks.TCPClient;

import java.io.IOException;
import java.util.List;

@Slf4j
public class InvoiceService {
    private final TCPClient tcpClient;
    private final Gson gson;

    public InvoiceService(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
        this.gson = new Gson();
    }

    /**
     * Tạo hóa đơn mới
     */
    public Invoice createInvoice(InvoiceCreationRequest request) throws Exception {
        try {
            String jsonPayload = gson.toJson(request);
            String response = tcpClient.sendRequest("CREATE_INVOICE", jsonPayload);

            // Check if error
            if (response.startsWith("Error:")) {
                throw new Exception(response);
            }

            return gson.fromJson(response, Invoice.class);

        } catch (IOException e) {
            log.error("Error creating invoice: ", e);
            throw new Exception("Không thể kết nối đến server: " + e.getMessage());
        }
    }

    /**
     * Lấy tất cả hóa đơn
     */
    public List<Invoice> getAllInvoices() throws Exception {
        try {
            String response = tcpClient.sendRequest("GET_ALL_INVOICES", "{}");

            if (response.startsWith("Error:")) {
                throw new Exception(response);
            }

            return gson.fromJson(response, new TypeToken<List<Invoice>>(){}.getType());

        } catch (IOException e) {
            log.error("Error getting invoices: ", e);
            throw new Exception("Không thể kết nối đến server: " + e.getMessage());
        }
    }

    /**
     * Lấy chi tiết hóa đơn theo ID
     */
    public Invoice getInvoiceById(String id) throws Exception {
        try {
            String response = tcpClient.sendRequest("GET_INVOICE", id);

            if (response.startsWith("Error:")) {
                throw new Exception(response);
            }

            return gson.fromJson(response, Invoice.class);

        } catch (IOException e) {
            log.error("Error getting invoice: ", e);
            throw new Exception("Không thể kết nối đến server: " + e.getMessage());
        }
    }
}
