package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CartItem {

    @NotBlank private String id;
    @NotNull private Integer quantity;

}
