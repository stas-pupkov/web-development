package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartShow {

    private int orderNumber;
    private String productName;
    private int quantities;
    private Double subtotal;
}
