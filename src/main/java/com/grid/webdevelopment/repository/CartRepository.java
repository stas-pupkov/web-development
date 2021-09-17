package com.grid.webdevelopment.repository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartRepository {

    Map<String, Integer> cart = new HashMap<>();

    public void save(String id, Integer quantity) {
        cart.put(id, quantity);
    }

    public Map<String, Integer> getAll() {
        return cart;
    }

    public Integer getQuantity(String id) {
        return cart.get(id);
    }

    public boolean delete(String id, Integer quantity) {
        return cart.remove(id, quantity);
    }

}
