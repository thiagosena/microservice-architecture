package com.thiagosena.orderservice.controllers.exceptions;

public class ProductNotInStockException extends RuntimeException {
    public ProductNotInStockException(String message) {
        super(message);
    }
}