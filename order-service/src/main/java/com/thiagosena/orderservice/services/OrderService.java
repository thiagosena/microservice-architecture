package com.thiagosena.orderservice.services;

import com.thiagosena.orderservice.controllers.payloads.OrderRequest;
import com.thiagosena.orderservice.controllers.payloads.OrderResponse;
import com.thiagosena.orderservice.models.Order;
import com.thiagosena.orderservice.models.OrderLineItems;
import com.thiagosena.orderservice.repositories.OrderRepository;
import com.thiagosena.orderservice.services.clients.InventoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsRequestList()
                .stream()
                .map(OrderLineItems::of).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode).toList();

        // Call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventories",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if (inventoryResponseArray == null || inventoryResponseArray.length == 0) {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if (!allProductsInStock) {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }

        Order orderSaved = orderRepository.save(order);
        log.info("Successfully saved order {}", orderSaved);
        return OrderResponse.of(orderSaved);

    }
}