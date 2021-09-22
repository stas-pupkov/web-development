package com.grid.webdevelopment.repository;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Data
@Service
@SessionScope
public class CartItemsRepository {

    // userId     productId quantity
    Map<String, Map<String, Integer>> items = new HashMap<>();

    public void save(String userId, Map<String, Integer> item) {
        items.put(userId, item);
    }

    public Map<String, Integer> getItems(String userId) {
        items.putIfAbsent(userId, new HashMap<>());
        return items.get(userId);
    }

    public Integer getQuantity(String userId, String productId) {
        return items.get(userId).get(productId);
    }

    public boolean delete(String userId, String productId, Integer quantity) {
        return items.get(userId).remove(productId, quantity);
    }
}
