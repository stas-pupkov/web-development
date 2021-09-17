package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {

    private String id;
    private String title;
    private int available;
    private String price;

}
