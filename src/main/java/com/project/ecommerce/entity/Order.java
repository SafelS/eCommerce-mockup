package com.project.ecommerce.entity;

import com.project.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

}
