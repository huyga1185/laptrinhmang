package com.ltm.ntn.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
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

    private List<InvoiceItem> items;
}