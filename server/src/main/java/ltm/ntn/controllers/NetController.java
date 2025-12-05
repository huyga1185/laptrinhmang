package ltm.ntn.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.services.InvoiceService;
import ltm.ntn.share.dto.requests.creations.InvoiceCreationRequest;

@Slf4j
public class NetController {
    private final InvoiceService invoiceService;

    private final Gson gson;

    public NetController() {
        this.invoiceService = new InvoiceService();
        this.gson = new Gson();
    }

    public String route(String command, String jsonPayload) {
        if (command == null || jsonPayload == null)
            return "Error: invalid command or json payload\n";

        switch (command) {
            case "CREATE_INVOICE":
                InvoiceCreationRequest request = gson.fromJson(jsonPayload, InvoiceCreationRequest.class);
                try {
                    return gson.toJson(invoiceService.createInvoice(request));
                } catch (Exception e) {
                    return "Error: " + e.getMessage() + "\n";
                }
            case "GET_ALL_INVOICES":
                try {
                    return gson.toJson(invoiceService.findAllInvoiceForClients());
                }  catch (Exception e) {
                    return "Error: " + e.getMessage() + "\n";
                }
            case "GET_INVOICE":
                try {
                    return gson.toJson(invoiceService.getInvoiceById(jsonPayload));
                } catch (Exception e) {
                    return "Error: " + e.getMessage() + "\n";
                }
            default:
                return "Error: invalid command\n";
        }
    }
}
