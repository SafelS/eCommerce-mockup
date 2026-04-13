package com.project.ecommerce.controller;

import com.project.ecommerce.dto.OrderRequestDto;
import com.project.ecommerce.dto.OrderResponseDto;
import com.project.ecommerce.enums.OrderStatus;
import com.project.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/my")
    public List<OrderResponseDto> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto requestDto){
        return orderService.createOrder(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public  OrderResponseDto updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status){
        return orderService.updateOrderStatus(status,id);
    }


}
