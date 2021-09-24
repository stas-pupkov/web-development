package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartView {

    private int orderNumber;
    private String productId;
    private String productName;
    private int quantities;
    private Double subtotal;

}
