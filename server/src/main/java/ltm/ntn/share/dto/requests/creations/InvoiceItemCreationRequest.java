package ltm.ntn.share.dto.requests.creations;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemCreationRequest {
    @SerializedName("product_id")
    private String productId;
    private int quantity;
}
