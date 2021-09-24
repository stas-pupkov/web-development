package com.grid.webdevelopment.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Cart {

    private Map<String, Integer> items = new HashMap<>();

}
