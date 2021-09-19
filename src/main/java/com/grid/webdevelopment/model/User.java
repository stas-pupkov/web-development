package com.grid.webdevelopment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotEmpty private String userId;
    @NotEmpty private String sessionId;
    @NotEmpty private String email;
    @NotEmpty private String password;
    private Status status;
    private Role role;

}
