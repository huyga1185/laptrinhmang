package ltm.ntn.models.dao.interfaces;

import ltm.ntn.models.pojo.Invoice;

import java.sql.Connection;
import java.util.List;

public interface IInvoiceDAO {
    Invoice save(Connection connection, Invoice invoice) throws Exception;
    Invoice getInvoiceById(String id) throws Exception;
    Invoice getInvoiceByInvoiceCode(String invoiceCode) throws Exception;
    List<Invoice> getAllInvoices() throws Exception;
}
