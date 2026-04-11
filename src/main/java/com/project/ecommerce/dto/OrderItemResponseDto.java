package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDto {

    private String productName;
    private int quantity;
    private double priceAtPurchase;

}
