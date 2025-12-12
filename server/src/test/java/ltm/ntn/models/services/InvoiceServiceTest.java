package ltm.ntn.models.services;

import lombok.extern.slf4j.Slf4j;
import ltm.ntn.share.dto.requests.creations.InvoiceCreationRequest;
import ltm.ntn.share.dto.requests.creations.InvoiceItemCreationRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InvoiceServiceTest {
    @Test
    public void createInvoice() {
        InvoiceService invoiceService = new InvoiceService();
        List<InvoiceItemCreationRequest> invoiceCreationRequests = new ArrayList<>();
        InvoiceItemCreationRequest invoiceItemCreationRequest = new InvoiceItemCreationRequest("0faa086c-0c64-4420-a968-615b701dfc59", 2);
        invoiceCreationRequests.add(invoiceItemCreationRequest);
        InvoiceCreationRequest request = InvoiceCreationRequest.builder()
                .invoiceCode("asdas")
                .items(invoiceCreationRequests)
                .build();
        try {
            invoiceService.createInvoice(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
