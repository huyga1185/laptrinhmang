package ltm.ntn.models.services.interfaces;

import ltm.ntn.models.pojo.Invoice;
import ltm.ntn.models.pojo.InvoiceItem;
import ltm.ntn.share.dto.requests.creations.InvoiceCreationRequest;
import ltm.ntn.share.dto.responses.creations.InvoiceCreationResponse;
import ltm.ntn.share.dto.responses.gettings.GetInvoiceResponse;

import java.util.List;

public interface IInvoiceService {
    Invoice createInvoice(Invoice invoice, List<InvoiceItem> items);
    Invoice findInvoiceById(String id);
    Invoice findInvoiceByInvoiceCode(String invoiceCode);
    List<Invoice> findAllInvoices();
    InvoiceCreationResponse createInvoice(InvoiceCreationRequest request);
    GetInvoiceResponse getInvoiceById(String id);
    List<GetInvoiceResponse> findAllInvoiceForClients();
    int getTotalInvoices();
    double getRevenue();
    List<GetInvoiceResponse> findAllInvoicesSafe();
}