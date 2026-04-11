package com.project.ecommerce.dto;

import com.project.ecommerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> orderItems;

}
