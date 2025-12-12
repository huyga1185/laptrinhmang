package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.CouponDAO;
import ltm.ntn.models.dao.InvoiceDAO;
import ltm.ntn.models.dao.InvoiceItemDAO;
import ltm.ntn.models.dao.ProductDAO;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.models.pojo.Invoice;
import ltm.ntn.models.pojo.InvoiceItem;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.interfaces.IInvoiceService;
import ltm.ntn.share.DBConnection;
import ltm.ntn.share.Utils;
import ltm.ntn.share.dto.requests.creations.InvoiceCreationRequest;
import ltm.ntn.share.dto.requests.creations.InvoiceItemCreationRequest;
import ltm.ntn.share.dto.responses.creations.InvoiceCreationResponse;
import ltm.ntn.share.dto.responses.creations.InvoiceItemCreationResponse;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceItemResponse;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;
import ltm.ntn.share.enums.DiscountType;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

@Slf4j
public class InvoiceService implements IInvoiceService {
    private final InvoiceDAO invoiceDAO;
    private final InvoiceItemDAO invoiceItemDAO;
    private final CouponDAO couponDAO;
    private final ProductDAO productDAO;

    private final ProductService productService;
    private final InvoiceItemService invoiceItemService;
    private final CouponService couponService;

    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceItemDAO = new InvoiceItemDAO();
        this.couponDAO = new CouponDAO();
        this.productDAO = new ProductDAO();

        this.productService = new ProductService();
        this.invoiceItemService = new InvoiceItemService();
        this.couponService = new CouponService();
    }

    @Override
    public InvoiceCreationResponse createInvoice(InvoiceCreationRequest request) {
        Invoice invoice = Invoice.builder()
                .couponId(request.getCouponId())
                .invoiceCode(request.getInvoiceCode())
                .build();

        List<InvoiceItemCreationRequest> items = request.getItems();
        List<InvoiceItem> itemList = new ArrayList<>();
        for (InvoiceItemCreationRequest item : items) {
            Product product = productService.findProductById(item.getProductId());

            if (product == null)
                throw new NullPointerException("Product not found");

            InvoiceItem invoiceItem = InvoiceItem.builder()
                    .productId(item.getProductId())
                    .quantity(item.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            itemList.add(invoiceItem);
        }

        invoice = createInvoice(invoice, itemList);

        if (invoice == null)
            throw new RuntimeException("Could not create invoice");

        List<InvoiceItem> its = invoiceItemService.findInvoiceItemsByInvoiceId(invoice.getId());

        List<InvoiceItemCreationResponse> itcps = new ArrayList<>();
        for (InvoiceItem it : its) {
            Product p = productService.findProductById(it.getProductId());

            if (p == null)
                throw new NullPointerException("Product not found");

            InvoiceItemCreationResponse itcr = InvoiceItemCreationResponse.builder()
                    .productName(p.getName())
                    .quantity(it.getQuantity())
                    .unitPrice(it.getUnitPrice())
                    .totalPrice(it.getTotalPrice())
                    .build();

            itcps.add(itcr);
        }

        InvoiceCreationResponse response = null;
        if (invoice.getCouponId() == null) {
            response = InvoiceCreationResponse.builder()
                    .invoiceCode(invoice.getInvoiceCode())
                    .totalAmount(invoice.getTotalAmount())
                    .createdAt(invoice.getCreatedAt())
                    .items(itcps)
                    .build();
        } else {
            Coupon c = couponService.findCouponByID(invoice.getCouponId());

            if (c == null)
                throw new NullPointerException("Coupon not found");

            response = InvoiceCreationResponse.builder()
                    .invoiceCode(invoice.getInvoiceCode())
                    .totalAmount(invoice.getTotalAmount())
                    .createdAt(invoice.getCreatedAt())
                    .items(itcps)
                    .couponCode(c.getCouponCode())
                    .discountType(c.getDiscountType().toString())
                    .discountAmount(c.getDiscountAmount())
                    .build();
        }

        if (response == null)
            throw new RuntimeException("Could not create invoice");

        return response;
    }


    private GetInvoiceResponse mapInvoiceToGetInvoiceResponse(Invoice invoice, List<GetInvoiceItemResponse> giips) {
        GetInvoiceResponse gir;
        if (invoice.getCouponId() == null) {
            gir = GetInvoiceResponse.builder()
                    .invoiceCode(invoice.getInvoiceCode())
                    .totalAmount(invoice.getTotalAmount())
                    .createdAt(invoice.getCreatedAt())
                    .items(giips)
                    .build();
        } else {
            Coupon c = couponService.findCouponByID(invoice.getCouponId());

            if (c == null)
                throw new NullPointerException("Coupon not found");

            gir = GetInvoiceResponse.builder()
                    .invoiceCode(invoice.getInvoiceCode())
                    .totalAmount(invoice.getTotalAmount())
                    .createdAt(invoice.getCreatedAt())
                    .items(giips)
                    .couponCode(c.getCouponCode())
                    .discountType(c.getDiscountType().toString())
                    .discountAmount(c.getDiscountAmount())
                    .build();
        }
        return gir;
    }

    @Override
    public GetInvoiceResponse getInvoiceById(String id) {
        Invoice invoice = findInvoiceById(id);

        if (invoice == null)
            throw new RuntimeException("Could not find invoice");

        List<InvoiceItem> its = invoiceItemService.findInvoiceItemsByInvoiceId(invoice.getId());

        if (its == null || its.isEmpty())
            throw new RuntimeException("Could not find invoice");

        List<GetInvoiceItemResponse> giips = new ArrayList<>();
        for (InvoiceItem it : its) {
            Product p = productService.findProductById(it.getProductId());

            if (p == null)
                throw new NullPointerException("Product not found");

            GetInvoiceItemResponse giip = GetInvoiceItemResponse.builder()
                    .productName(p.getName())
                    .quantity(it.getQuantity())
                    .unitPrice(it.getUnitPrice())
                    .totalPrice(it.getTotalPrice())
                    .build();

            giips.add(giip);
        }

        GetInvoiceResponse response = mapInvoiceToGetInvoiceResponse(invoice, giips);

        if (response == null)
            throw new RuntimeException("Could not find any invoice");

        return response;
    }

    @Override
    public List<GetInvoiceResponse> findAllInvoicesSafe() {
        List<Invoice> invoices = findAllInvoices(); // lấy tất cả invoices từ DB

        List<GetInvoiceResponse> responses = new ArrayList<>();

        if (invoices == null || invoices.isEmpty()) {
            // Không ném exception, trả về danh sách rỗng
            return responses;
        }

        for (Invoice invoice : invoices) {
            List<InvoiceItem> items = invoiceItemService.findInvoiceItemsByInvoiceId(invoice.getId());
            List<GetInvoiceItemResponse> itemResponses = new ArrayList<>();

            if (items != null && !items.isEmpty()) {
                for (InvoiceItem item : items) {
                    Product p = productService.findProductById(item.getProductId());

                    GetInvoiceItemResponse itemResponse = GetInvoiceItemResponse.builder()
                            .productName(p != null ? p.getName() : "Unknown")
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .totalPrice(item.getTotalPrice())
                            .build();

                    itemResponses.add(itemResponse);
                }
            }

            GetInvoiceResponse invoiceResponse = mapInvoiceToGetInvoiceResponse(invoice, itemResponses);
            responses.add(invoiceResponse);
        }

        return responses;
    }


    @Override
    public List<GetInvoiceResponse> findAllInvoiceForClients() {
        List<Invoice> invoices = findAllInvoices();

        if (invoices == null || invoices.isEmpty())
            throw new RuntimeException("Could not find any invoices");

        List<GetInvoiceResponse> girs = new ArrayList<>();

        for (Invoice invoice : invoices) {
            List<InvoiceItem> its = invoiceItemService.findInvoiceItemsByInvoiceId(invoice.getId());
            List<GetInvoiceItemResponse> giips = new ArrayList<>();

            if (its == null || its.isEmpty())
                throw new RuntimeException("Could not find any invoice items");

            for (InvoiceItem it : its) {
                Product p = productService.findProductById(it.getProductId());

                GetInvoiceItemResponse giip = GetInvoiceItemResponse.builder()
                        .productName(p.getName())
                        .quantity(it.getQuantity())
                        .unitPrice(it.getUnitPrice())
                        .totalPrice(it.getTotalPrice())
                        .build();

                giips.add(giip);
            }

            GetInvoiceResponse gir = mapInvoiceToGetInvoiceResponse(invoice, giips);;

            if (gir == null)
                throw new RuntimeException("Could not find any invoice items");

            girs.add(gir);
        }

        return girs;
    }

    @Override
    public int getTotalInvoices() {
        return findAllInvoices().size();
    }

    @Override
    public double getRevenue() {
        List<Invoice> invoices = findAllInvoices();
        double totalRevenue = 0;
        if  (invoices == null || invoices.isEmpty())
            return 0;
        for (Invoice invoice : invoices)
            totalRevenue += invoice.getTotalAmount();
        return totalRevenue;
    }

    @Override
    public Invoice createInvoice(Invoice invoice, List<InvoiceItem> items) {
        if (invoice == null) throw new NullPointerException("Invoice must not be null.");
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("Invoice items must not be empty.");

        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            if (invoice.getId() != null)
                throw new RuntimeException("Invoice ID must be null for new invoice");

            Coupon c = null;
            if (invoice.getCouponId() != null) {
                c = couponService.findCouponByID(invoice.getCouponId());
                if (c == null) throw new RuntimeException("Coupon with ID " + invoice.getCouponId() + " not found.");
                // Kiểm tra coupon hợp lệ
                if (c.getIssueDate().isAfter(LocalDate.now()))
                    throw new RuntimeException("Coupon not active yet");
                if (c.getExpiryDate().isBefore(LocalDate.now()))
                    throw new RuntimeException("Coupon expired");
                if (c.getRedemptions() >= c.getMaxRedemptions())
                    throw new RuntimeException("Coupon redemptions exceeded");
            }

            // Insert invoice
            Invoice savedInvoice = invoiceDAO.save(connection, invoice);
            log.debug("Inserted invoice with ID = {}", savedInvoice.getId());

            double totalPrice = 0.0;
            for (InvoiceItem item : items) {
                Product product = productService.findProductById(item.getProductId());
                if (product == null) throw new RuntimeException("Product not found: " + item.getProductId());
                if (!product.isActive()) throw new RuntimeException("Product not active: " + product.getName());
                if (item.getQuantity() > product.getQuantity()) throw new RuntimeException("Quantity exceeds: " + product.getName());

                item.setInvoiceId(savedInvoice.getId());
                item.setUnitPrice(product.getPrice());
                item.setTotalPrice(item.getUnitPrice() * item.getQuantity());

                invoiceItemDAO.save(connection, item);

                product.setQuantity(product.getQuantity() - item.getQuantity());
                product.setSold(product.getSold() + item.getQuantity());
                productDAO.save(connection, product);

                totalPrice += item.getTotalPrice();
            }

            // Áp dụng coupon nếu có
            if (c != null) {
                if (c.getDiscountType().equals(DiscountType.PERCENTAGE)) {
                    totalPrice = totalPrice - (totalPrice * (c.getDiscountAmount() / 100.0));
                } else if (c.getDiscountType().equals(DiscountType.FIXED_AMOUNT)) {
                    totalPrice = totalPrice - c.getDiscountAmount();
                    if (totalPrice < 0) totalPrice = 0;
                }

                // Tăng redemptions luôn
                c.setRedemptions(c.getRedemptions() + 1);
                couponDAO.increaseRedemptions(connection, c);
            }

            savedInvoice.setTotalAmount(totalPrice);
            invoiceDAO.save(connection, savedInvoice);

            connection.commit();

            return findInvoiceById(savedInvoice.getId());
        } catch (Exception e) {
            try { if (connection != null) connection.rollback(); } catch (SQLException ex) {}
            throw new RuntimeException("Could not create invoice.", e);
        } finally {
            try { if (connection != null) connection.close(); } catch (SQLException ignore) {}
        }
    }



    @Override
    public Invoice findInvoiceById(String id) {
        try {
            return invoiceDAO.getInvoiceById(id);
        } catch (Exception e) {
            log.error("Error finding invoice by id: ", e);
            throw new RuntimeException("Could not find invoice by id");
        }
    }

    @Override
    public Invoice findInvoiceByInvoiceCode(String invoiceCode) {
        try {
            return invoiceDAO.getInvoiceByInvoiceCode(invoiceCode);
        } catch (Exception e) {
            log.error("Error finding invoice by invoice code: ", e);
            throw new RuntimeException("Could not find invoice by invoice code");
        }
    }

    @Override
    public List<Invoice> findAllInvoices() {
        try {
            return invoiceDAO.getAllInvoices();
        } catch (Exception e) {
            log.error("Error finding invoices: ", e);
            return new ArrayList<>();
        }
    }

    public Hashtable<String, Double> getRevenueByMonth(int year) {
        List<Invoice> invoices = findAllInvoices();
        Hashtable<String, Double> revenueByMonth = new Hashtable<>();

        // Khởi tạo tất cả tháng = 0
        for (int m = 1; m <= 12; m++) {
            revenueByMonth.put(String.format("%02d", m), 0.0);
        }

        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

        for (Invoice invoice : invoices) {
            if (invoice.getCreatedAt() != null && invoice.getCreatedAt().getYear() == year) {
                String month = invoice.getCreatedAt().format(monthFormatter);
                double current = revenueByMonth.getOrDefault(month, 0.0);
                revenueByMonth.put(month, current + invoice.getTotalAmount());
            }
        }

        return revenueByMonth;
    }
}