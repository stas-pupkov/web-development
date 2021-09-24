package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Product {

    @NotBlank private String id;
    @NotBlank private String title;
    @NotNull private int available;
    @NotNull private Double price;

}
