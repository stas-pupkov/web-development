package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItem {

    private String id;
    private Integer quantity;

}
