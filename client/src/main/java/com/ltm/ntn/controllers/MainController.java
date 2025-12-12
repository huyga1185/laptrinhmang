package com.ltm.ntn.controllers;

import com.google.gson.*;
import com.ltm.ntn.service.ProductService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import com.ltm.ntn.networks.TCPClient;
import com.ltm.ntn.service.ExportService;
import com.ltm.ntn.service.InvoiceService;
import com.ltm.ntn.views.MainView;

import javax.swing.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Slf4j
@Getter
public class MainController {
    private final MainView mainView;
    private final TCPClient tcpClient;

    private final ProductService productService;
    private final InvoiceService invoiceService;
    private final ExportService exportService;

    private final ProductController productController;
    private final CreateInvoiceController createInvoiceController;
    private final InvoiceController invoiceController;

    private final Gson gson;

    public MainController() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString()); // ISO-8601 string
                    }
                })
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString());
                    }
                })
                .create();


        // Initialize services
        this.tcpClient = new TCPClient();
        this.invoiceService = new InvoiceService(tcpClient, gson);
        this.exportService = new ExportService();
        this.productService = new ProductService(tcpClient, gson);

        // Initialize controllers
        this.productController = new ProductController(productService);
        this.createInvoiceController = new CreateInvoiceController(productService, invoiceService);
        this.invoiceController = new InvoiceController(
                invoiceService,
                exportService,
                () -> showInvoiceList() // Callback to return to list
        );

        // Initialize main view
        this.mainView = new MainView();

        // Add views to card layout
        setupViews();

        // Setup menu listeners
        setupMenuListeners();

        // Connect to server
        connectToServer();

        // Show default view
        mainView.showCard("products");
    }

    private void setupViews() {
        mainView.getContentPanel().add(productController.getView(), "products");
        mainView.getContentPanel().add(createInvoiceController.getView(), "createInvoice");
        mainView.getContentPanel().add(invoiceController.getListView(), "invoices");
        mainView.getContentPanel().add(invoiceController.getDetailView(), "invoiceDetail");
    }

    private void setupMenuListeners() {
        mainView.getBtnViewProducts().addActionListener(e -> {
            productController.loadProducts();
            mainView.showCard("products");
        });

        mainView.getBtnCreateInvoice().addActionListener(e -> {
            createInvoiceController.loadProducts();
            mainView.showCard("createInvoice");
        });

        mainView.getBtnViewInvoices().addActionListener(e -> {
            invoiceController.loadInvoices();
            mainView.showCard("invoices");
        });

        // Listener để chuyển từ list sang detail
        invoiceController.getListView().getBtnViewDetail().addActionListener(e -> {
            var selected = invoiceController.getListView()
                    .getSelectedInvoice(invoiceController.getInvoices());

            if (selected != null) {
                invoiceController.getDetailView().displayInvoice(selected);
                mainView.showCard("invoiceDetail");
            } else {
                JOptionPane.showMessageDialog(mainView,
                        "Vui lòng chọn một hóa đơn!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void showInvoiceList() {
        mainView.showCard("invoices");
    }

    private void connectToServer() {
        try {
            tcpClient.connect();
            log.info("Connected to server successfully");
        } catch (Exception e) {
            log.error("Failed to connect to server: ", e);
            JOptionPane.showMessageDialog(mainView,
                    "Không thể kết nối đến server!\n" +
                            "Vui lòng kiểm tra server đã chạy chưa.\n\n" +
                            "Lỗi: " + e.getMessage(),
                    "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void show() {
        mainView.setVisible(true);
    }

    public void shutdown() {
        tcpClient.disconnect();
        log.info("Application shutdown");
    }
}

