package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class Cart {

    private String orderId;
    private LocalDateTime date;
    private Double total;
    private OrderStatus status;

    public enum OrderStatus {
        PROGRESS,
        CANCELED;
    }
}
