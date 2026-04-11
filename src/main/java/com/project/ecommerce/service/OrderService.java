package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderItemResponseDto;
import com.project.ecommerce.dto.OrderRequestDto;
import com.project.ecommerce.dto.OrderResponseDto;
import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderItem;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.enums.OrderStatus;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository  userRepository;

    public List<OrderResponseDto> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(o -> new OrderResponseDto(o.getId(),o.getStatus(),o.getCreatedAt(),o.getOrderItems()
                        .stream().map(oi -> new OrderItemResponseDto(oi.getProduct().getName(), oi.getQuantity(), oi.getPriceAtPurchase()))
                        .toList()))
                .toList();

        return orderResponseDtos;

    }

    public OrderResponseDto getOrderById(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new OrderResponseDto(order.getId(),order.getStatus(),order.getCreatedAt(),order.getOrderItems().stream()
                .map(oi -> new OrderItemResponseDto(oi.getProduct().getName(), oi.getQuantity(), oi.getPriceAtPurchase()))
                .toList());

    }

    //TODO
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Order order = new Order();
        order.setStatus(OrderStatus.PAYED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(user);
        order.setOrderItems(orderRequestDto.getOrderItems().stream()
                .map(oi -> new OrderItem()));

    }



}
