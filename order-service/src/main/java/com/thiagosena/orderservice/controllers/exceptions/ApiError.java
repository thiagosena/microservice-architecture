package com.thiagosena.orderservice.controllers.exceptions;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record ApiError(
        Timestamp timestamp,
        int status,
        String error

) {
    public ApiError(int status, String error) {
        this(Timestamp.valueOf(LocalDateTime.now()), status, error);
    }
}