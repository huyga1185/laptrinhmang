package ltm.ntn.share.dto.responses.creations;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceCreationResponse {
    @SerializedName("invoice_code")
    private String invoiceCode;
    @SerializedName("discount_type")
    private String discountType;
    @SerializedName("discount_amount")
    private Double discountAmount;
    @SerializedName("coupon_code")
    private String couponCode;
    @SerializedName("total_amount")
    private double totalAmount;
    @SerializedName("created_at")
    private LocalDateTime createdAt;
    private List<InvoiceItemCreationResponse> items;
}
