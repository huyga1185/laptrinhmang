package com.ltm.ntn.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreationRequest {
    @SerializedName("coupon_id")
    private String couponId;

    @SerializedName("invoice_code")
    private String invoiceCode;

    private List<InvoiceItemRequest> items;
}
