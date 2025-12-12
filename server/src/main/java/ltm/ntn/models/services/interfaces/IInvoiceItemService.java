package ltm.ntn.models.services.interfaces;

import ltm.ntn.models.pojo.InvoiceItem;

import java.util.List;

public interface IInvoiceItemService {
    List<InvoiceItem> findAllInvoiceItems();
    List<InvoiceItem> findInvoiceItemsByInvoiceCode(String invoiceCode);
    List<InvoiceItem> findInvoiceItemsByInvoiceId(String id);
    List<InvoiceItem> findInvoiceItemsByProductId(String productId);
    InvoiceItem findInvoiceItemById(String id);
    boolean isProductUsed(String productId);
}
