package com.grid.webdevelopment.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class User {

    private String id = UUID.randomUUID().toString();

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    private Status status = Status.ACTIVE;
    private Role role = Role.USER;

    private List<Product> products;
    private List<CartItem> cart;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
