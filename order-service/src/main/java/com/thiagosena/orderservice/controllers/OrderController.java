package com.thiagosena.orderservice.controllers;

import com.thiagosena.orderservice.controllers.payloads.OrderRequest;
import com.thiagosena.orderservice.controllers.payloads.OrderResponse;
import com.thiagosena.orderservice.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(orderRequest);
    }
}