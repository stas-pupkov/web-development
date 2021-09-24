package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class AccessRequest {

    @NotBlank private String email;
    @NotBlank private String password;

}
