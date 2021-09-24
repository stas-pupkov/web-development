package com.grid.webdevelopment.security;

import lombok.Getter;

@Getter
public enum Permission {

    READ("users:read"),
    WRITE("users:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
