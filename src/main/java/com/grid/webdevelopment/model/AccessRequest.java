package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
public class AccessRequest {

    @NotEmpty(message = "Email can't be empty")
    private String email;

    @NotEmpty(message = "Password can't be less than 3 or more than 10 symbols")
    @Size(min = 3, max = 10)
    private String password;

}
