package com.thiagosena.orderservice.services;

import com.thiagosena.orderservice.controllers.exceptions.ProductNotInStockException;
import com.thiagosena.orderservice.controllers.payloads.OrderRequest;
import com.thiagosena.orderservice.controllers.payloads.OrderResponse;
import com.thiagosena.orderservice.events.OrderPlacedEvent;
import com.thiagosena.orderservice.models.Order;
import com.thiagosena.orderservice.models.OrderLineItems;
import com.thiagosena.orderservice.repositories.OrderRepository;
import com.thiagosena.orderservice.services.clients.InventoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Value("${inventory.url}")
    private String inventoryUrl;

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsRequestList()
                .stream()
                .map(OrderLineItems::of).toList();
        order.setOrderLineItemsList(orderLineItems);

        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");

        try (Tracer.SpanInScope ignored = tracer.withSpan(inventoryServiceLookup.start())) {
            inventoryServiceLookup.tag("call", "inventory-service");

            log.info("Calling inventory service");
            // Call inventory service and place order if product is in stock
            InventoryResponse[] inventoryResponseArray = getInventory(order);

            if (inventoryResponseArray == null || inventoryResponseArray.length == 0) {
                throw new ProductNotInStockException("Product is not in stock, please try again later");
            }

            boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

            if (!allProductsInStock) {
                throw new ProductNotInStockException("Product is not in stock, please try again later");
            }

            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            Order orderSaved = orderRepository.save(order);
            log.info("Successfully saved order {}", orderSaved);
            return OrderResponse.of(orderSaved);
        } finally {
            inventoryServiceLookup.end();
        }
    }

    private InventoryResponse[] getInventory(Order order) {
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode).toList();

        return webClientBuilder.build().get()
                .uri(inventoryUrl,
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
    }

}