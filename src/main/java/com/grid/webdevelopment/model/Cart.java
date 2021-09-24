package com.grid.webdevelopment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
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
