package com.project.ecommerce.service;

import com.project.ecommerce.dto.OrderItemRequestDto;
import com.project.ecommerce.dto.OrderItemResponseDto;
import com.project.ecommerce.dto.OrderRequestDto;
import com.project.ecommerce.dto.OrderResponseDto;
import com.project.ecommerce.entity.Order;
import com.project.ecommerce.entity.OrderItem;
import com.project.ecommerce.entity.Product;
import com.project.ecommerce.entity.User;
import com.project.ecommerce.enums.OrderStatus;
import com.project.ecommerce.repository.OrderItemRepository;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;


    //ADMIN ONLY
    public List<OrderResponseDto> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(o -> new OrderResponseDto(o.getId(),o.getStatus(),o.getCreatedAt(),o.getOrderItems()
                        .stream().map(oi -> new OrderItemResponseDto(oi.getProduct().getName(), oi.getQuantity(), oi.getPriceAtPurchase()))
                        .toList()))
                .toList();

        return orderResponseDtos;

    }


    public List<OrderResponseDto> getMyOrders(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Order> myOrders = orderRepository.findByUserId(user.getId());
        List<OrderResponseDto> orderResponseDtos = myOrders.stream()
                .map(o -> new OrderResponseDto(o.getId(),o.getStatus(),o.getCreatedAt(),o.getOrderItems().stream()
                        .map(oi -> new OrderItemResponseDto(oi.getProduct().getName(), oi.getQuantity(), oi.getPriceAtPurchase()))
                        .toList()))
                .toList();

        return orderResponseDtos;
    }


    public OrderResponseDto updateOrderStatus(OrderStatus status, Long id){

        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        return new OrderResponseDto(updatedOrder.getId(),updatedOrder.getStatus(),updatedOrder.getCreatedAt(),updatedOrder.getOrderItems().stream()
                .map(oi -> new OrderItemResponseDto(oi.getProduct().getName(),oi.getQuantity(),oi.getPriceAtPurchase())).toList());

    }


    public OrderResponseDto getOrderById(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new OrderResponseDto(order.getId(),order.getStatus(),order.getCreatedAt(),order.getOrderItems().stream()
                .map(oi -> new OrderItemResponseDto(oi.getProduct().getName(), oi.getQuantity(), oi.getPriceAtPurchase()))
                .toList());

    }


    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(user);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDto itemDto : orderRequestDto.getOrderItems()){
            Product product = productRepository.findById(itemDto.getProductId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setQuantity(itemDto.getQuantity());
            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            orderItems.add(orderItem);

        }

        orderItemRepository.saveAll(orderItems);

        return new OrderResponseDto(savedOrder.getId(), savedOrder.getStatus(), savedOrder.getCreatedAt(), orderItems.stream()
                .map(item -> new OrderItemResponseDto(item.getProduct().getName(), item.getQuantity(), item.getPriceAtPurchase())).toList());

    }



}
