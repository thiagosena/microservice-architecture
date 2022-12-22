package com.thiagosena.orderservice.services.clients;

public record InventoryResponse(String skuCode, boolean isInStock) {
    @Override
    public String toString() {
        return "InventoryResponse{" +
                "skuCode='" + skuCode + '\'' +
                ", isInStock=" + isInStock +
                '}';
    }
}