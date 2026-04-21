package com.project.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemRequestDto {

    @Positive(message = "Product ID not valid")
    private Long productId;

    @Positive(message = "Quantity not valid")
    private int quantity;

}
