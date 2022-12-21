package com.thiagosena.orderservice.controllers.payloads;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderLineItemsRequest(
        UUID id,
        String skuCode,
        BigDecimal price,
        Integer quantity
) {
}