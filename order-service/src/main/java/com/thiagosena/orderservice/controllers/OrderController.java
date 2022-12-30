package com.thiagosena.orderservice.controllers;

import com.thiagosena.orderservice.controllers.exceptions.ApiError;
import com.thiagosena.orderservice.controllers.exceptions.ProductNotInStockException;
import com.thiagosena.orderservice.controllers.payloads.OrderRequest;
import com.thiagosena.orderservice.controllers.payloads.OrderResponse;
import com.thiagosena.orderservice.services.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeOrderFallback")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

    private CompletableFuture<ApiError> placeOrderFallback(OrderRequest orderRequest, Throwable throwable) {
        log.error("Error when call inventory service", throwable);
        return CompletableFuture.supplyAsync(() -> new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Oops! Something went wrong, please order after some time!"
        ));
    }

    private CompletableFuture<ApiError> placeOrderFallback(ProductNotInStockException ex) {
        return CompletableFuture.supplyAsync(() -> new ApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

}