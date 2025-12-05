package ltm.ntn.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InvoiceItem {
    private String id;
    private String invoiceId;
    private String productId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}
