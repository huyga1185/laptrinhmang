package ltm.ntn.models.dao.interfaces;

import ltm.ntn.models.pojo.InvoiceItem;

import java.sql.Connection;
import java.util.List;

public interface IInvoiceItemDAO {
    InvoiceItem save(Connection connection, InvoiceItem invoiceItem) throws Exception;
    List<InvoiceItem> getAllInvoiceItemsByInvoiceCode(String invoiceCode) throws Exception;
    List<InvoiceItem> getAllInvoiceItemsByInvoiceId(String invoiceId) throws Exception;
    List<InvoiceItem> getAllInvoiceItemsByProductId(String productId) throws Exception;
    List<InvoiceItem> getAllInvoiceItems() throws Exception;
    InvoiceItem getInvoiceItemById(String id) throws Exception;
    boolean isProductUsed(String productId) throws Exception;
}
