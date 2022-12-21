package com.thiagosena.orderservice.controllers.payloads;

import com.thiagosena.orderservice.models.OrderLineItems;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderLineItemsResponse(
        UUID id,
        String skuCode,
        BigDecimal price,
        Integer quantity
) {
    public static OrderLineItemsResponse of(OrderLineItems orderLineItems) {
        return OrderLineItemsResponse.builder()
                .id(orderLineItems.getId())
                .skuCode(orderLineItems.getSkuCode())
                .price(orderLineItems.getPrice())
                .quantity(orderLineItems.getQuantity())
                .build();
    }
}