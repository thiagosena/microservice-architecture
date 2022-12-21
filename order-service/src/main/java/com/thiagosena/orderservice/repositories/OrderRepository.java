package com.thiagosena.orderservice.repositories;

import com.thiagosena.orderservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}