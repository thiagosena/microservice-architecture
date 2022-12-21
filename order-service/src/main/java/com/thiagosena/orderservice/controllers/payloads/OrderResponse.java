package com.thiagosena.orderservice.controllers.payloads;

import com.thiagosena.orderservice.models.Order;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID id,
        String orderNumber,
        List<OrderLineItemsResponse> orderLineItemsResponseList
) {
    public static OrderResponse of(Order orderSaved) {
        return OrderResponse.builder()
                .id(orderSaved.getId())
                .orderNumber(orderSaved.getOrderNumber())
                .orderLineItemsResponseList(orderSaved.getOrderLineItemsList().stream().map(OrderLineItemsResponse::of).toList())
                .build();
    }
}