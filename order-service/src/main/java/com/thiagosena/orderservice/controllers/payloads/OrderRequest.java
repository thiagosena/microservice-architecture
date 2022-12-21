package com.thiagosena.orderservice.controllers.payloads;

import java.util.List;

public record OrderRequest(List<OrderLineItemsRequest> orderLineItemsRequestList) {
}