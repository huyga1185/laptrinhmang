package com.ltm.ntn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String sku;
    private String description;
    private double price;
    private int quantity;
    private int sold;
    private boolean isActive;
}