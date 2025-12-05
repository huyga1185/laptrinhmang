package ltm.ntn.share.dto.requests.creations;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
public class InvoiceCreationRequest {
    @SerializedName("coupon_id")
    private String couponId;
    @SerializedName("invoice_code")
    private String invoiceCode;
    List<InvoiceItemCreationRequest> items;
}
