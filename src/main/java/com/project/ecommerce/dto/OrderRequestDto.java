package com.project.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderRequestDto {

    private List<OrderItemRequestDto> orderItems;
}
