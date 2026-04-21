package com.project.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequestDto {

    @NotBlank(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequestDto> orderItems;
}
