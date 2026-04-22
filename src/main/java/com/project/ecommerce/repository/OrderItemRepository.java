package com.project.ecommerce.repository;

import com.project.ecommerce.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Override
    Page<OrderItem> findAll(Pageable pageable);
}
