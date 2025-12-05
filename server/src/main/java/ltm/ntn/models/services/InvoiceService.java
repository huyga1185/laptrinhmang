package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.CouponDAO;
import ltm.ntn.models.dao.InvoiceDAO;
import ltm.ntn.models.dao.InvoiceItemDAO;
import ltm.ntn.models.pojo.Coupon;
import ltm.ntn.models.pojo.Invoice;
import ltm.ntn.models.pojo.InvoiceItem;
import ltm.ntn.models.pojo.Product;
import ltm.ntn.models.services.interfaces.IInvoiceService;
import ltm.ntn.share.DBConnection;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class InvoiceService implements IInvoiceService {
    private final InvoiceDAO invoiceDAO;
    private final InvoiceItemDAO invoiceItemDAO;
    private final CouponDAO couponDAO;

    private final ProductService productService;
    private final InvoiceItemService invoiceItemService;
    private final CouponService couponService;

    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
        this.invoiceItemDAO = new InvoiceItemDAO();
        this.couponDAO = new CouponDAO();

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

        List<InvoiceItemCreationResponse> itcps  = new ArrayList<>();

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
        if (invoice == null)
            throw new NullPointerException("Invoice must not be null.");
        if (items.isEmpty())
            throw new IllegalArgumentException("Invoice items must not be empty.");

        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            double totalPrice = 0.0;
            Coupon coupon = null;

            invoice = invoiceDAO.save(connection, invoice);

            for (InvoiceItem item : items) {
                Product product = productService.findProductById(item.getProductId());

                if (product == null)
                    throw new RuntimeException("Could not find product id.");
                if (!product.isActive())
                    throw new RuntimeException("Product is not available.");
                if (item.getQuantity() > product.getQuantity())
                    throw new RuntimeException("Product quantity is greater than item quantity.");

                item.setTotalPrice(item.getUnitPrice() * item.getQuantity());
                invoiceItemDAO.save(connection, item);
                totalPrice += item.getTotalPrice();
            }

            if (invoice.getCouponId() != null) {
                coupon = couponService.findCouponByID(invoice.getCouponId());

                if (
                    !LocalDate.now().isBefore(coupon.getIssueDate()) &&
                    LocalDate.now().isBefore(coupon.getExpiryDate()) &&
                    (
                        coupon.getRedemptions() <
                        coupon.getMaxRedemptions()
                    ) &&
                    totalPrice >= coupon.getMinimumPurchaseAmount()
                ) {
                    if (coupon.getDiscountType() == DiscountType.PERCENTAGE)
                        totalPrice = totalPrice - (totalPrice * (coupon.getDiscountAmount() / 100.0));
                    else if (coupon.getDiscountType() == DiscountType.FIXED_AMOUNT)
                        totalPrice = totalPrice - coupon.getDiscountAmount();

                    coupon.setRedemptions(coupon.getRedemptions() + 1);
                    couponDAO.save(connection, coupon);
                }
            }

            invoice.setTotalAmount(totalPrice);
            invoiceDAO.save(connection, invoice);

            connection.commit();
        } catch (Exception e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                log.error("Error rolling back transaction: ", ex);
            }
            log.error("Error creating invoice: ", e);
            throw new RuntimeException("Could not create invoice.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                log.error("Error closing connection: ", e);
            }
        }
        return invoice;
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
}