package ltm.ntn.controllers;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import ltm.ntn.models.services.InvoiceService;
import ltm.ntn.models.services.ProductService;
import ltm.ntn.share.dto.requests.creations.InvoiceCreationRequest;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Slf4j
public class NetController {
    private final InvoiceService invoiceService;
    private final ProductService productService;

    private final Gson gson;

    public NetController() {
        this.invoiceService = new InvoiceService();
        this.productService = new ProductService();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString()); // ISO-8601
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
    }

    public String route(String command, String jsonPayload) {
        if (command == null || jsonPayload == null)
            return "Error: invalid command or json payload\n";

        switch (command) {
            case "GET_ALL_PRODUCTS":
                try {
                    String json = gson.toJson(productService.findAllActiveProducts());
                    log.info("Get all products: " + json);
                    return json;
                } catch (Exception e) {
                    return "Error:" + e.getMessage() + "\n";
                }
            case "CREATE_INVOICE":
                log.info("Creating invoice");
                InvoiceCreationRequest request = gson.fromJson(jsonPayload, InvoiceCreationRequest.class);
                try {
                    String json = gson.toJson(invoiceService.createInvoice(request));
                    log.info("Created invoice: " + json);
                    return json;
                } catch (Exception e) {
                    log.error("Error: " + e.getMessage() + "\n");
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
