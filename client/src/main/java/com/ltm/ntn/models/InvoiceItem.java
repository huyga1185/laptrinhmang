package com.ltm.ntn.models;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    @SerializedName("product_name")
    private String productName;

    private int quantity;

    @SerializedName("unit_price")
    private double unitPrice;

    @SerializedName("total_price")
    private double totalPrice;
}