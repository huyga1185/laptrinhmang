package ltm.ntn.share.dto.responses.creations;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceItemCreationResponse {
    @SerializedName("product_name")
    private String productName;
    private int quantity;
    @SerializedName("unit_price")
    private double unitPrice;
    @SerializedName("total_price")
    private double  totalPrice;
}
