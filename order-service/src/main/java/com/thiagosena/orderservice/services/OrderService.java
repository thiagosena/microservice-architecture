package com.thiagosena.orderservice.services;

import com.thiagosena.orderservice.controllers.payloads.OrderRequest;
import com.thiagosena.orderservice.controllers.payloads.OrderResponse;
import com.thiagosena.orderservice.models.Order;
import com.thiagosena.orderservice.models.OrderLineItems;
import com.thiagosena.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsRequestList()
                .stream()
                .map(OrderLineItems::of).toList();
        order.setOrderLineItemsList(orderLineItems);

        Order orderSaved = orderRepository.save(order);
        log.info("Successfully saved order {}", orderSaved);
        return OrderResponse.of(orderSaved);
    }
}