package com.thiagosena.inventoryservice.controllers.payloads;

import com.thiagosena.inventoryservice.models.Inventory;
import lombok.Builder;

@Builder
public record InventoryResponse(String skuCode, boolean isInStock) {
    public static InventoryResponse of(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}