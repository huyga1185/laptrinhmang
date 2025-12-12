package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.dao.InvoiceItemDAO;
import ltm.ntn.models.dao.interfaces.IInvoiceItemDAO;
import ltm.ntn.models.pojo.InvoiceItem;
import ltm.ntn.models.services.interfaces.IInvoiceItemService;

import java.util.List;

@Slf4j
public class InvoiceItemService implements IInvoiceItemService {
    private final IInvoiceItemDAO invoiceItemDAO;

    public InvoiceItemService() {
        this.invoiceItemDAO = new InvoiceItemDAO();
    }

    @Override
    public List<InvoiceItem> findAllInvoiceItems() {
        try {
            return invoiceItemDAO.getAllInvoiceItems();
        } catch (Exception e) {
            log.error("Could not find all invoice items: ", e);
            throw new RuntimeException("Could not find all invoice items");
        }
    }

    @Override
    public List<InvoiceItem> findInvoiceItemsByInvoiceCode(String invoiceCode) {
        try {
            return invoiceItemDAO.getAllInvoiceItemsByInvoiceCode(invoiceCode);
        } catch (Exception e) {
            log.error("Could not find invoice items by invoice code: ", e);
            throw new RuntimeException("Could not find invoice items by invoice code");
        }
    }

    @Override
    public List<InvoiceItem> findInvoiceItemsByInvoiceId(String id) {
        try {
            return invoiceItemDAO.getAllInvoiceItemsByInvoiceId(id);
        } catch (Exception e) {
            log.error("Could not find invoice items by invoice id: ", e);
            throw new RuntimeException("Could not find invoice items by invoice id");
        }
    }

    @Override
    public List<InvoiceItem> findInvoiceItemsByProductId(String productId) {
        try {
            return invoiceItemDAO.getAllInvoiceItemsByProductId(productId);
        } catch (Exception e) {
            log.error("Could not find invoice items by product id: ", e);
            throw new RuntimeException("Could not find invoice items by product id");
        }
    }

    @Override
    public InvoiceItem findInvoiceItemById(String id) {
        try {
            return invoiceItemDAO.getInvoiceItemById(id);
        } catch (Exception e) {
            log.error("Could not find invoice item by id: ", e);
            throw new RuntimeException("Could not find invoice item by id.");
        }
    }

    @Override
    public boolean isProductUsed(String productId) {
        try {
            return invoiceItemDAO.isProductUsed(productId);
        } catch (Exception e) {
            log.error("Could not find invoice items by product id: ", e);
            throw new RuntimeException("Could not find invoice items by product id");
        }
    }
}
