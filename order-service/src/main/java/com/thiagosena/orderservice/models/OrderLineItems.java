package com.thiagosena.orderservice.models;

import com.thiagosena.orderservice.controllers.payloads.OrderLineItemsRequest;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_line_items")
public class OrderLineItems {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "char(36)")
    @Type(type = "uuid-char")
    private UUID id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

    public static OrderLineItems of(OrderLineItemsRequest orderLineItemsRequest) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsRequest.skuCode())
                .price(orderLineItemsRequest.price())
                .quantity(orderLineItemsRequest.quantity())
                .build();
    }
}