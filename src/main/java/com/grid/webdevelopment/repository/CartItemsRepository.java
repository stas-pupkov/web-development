package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Data
@SessionScope
@Service
public class CartItemsRepository {

    private final UserService userService;

    private Map<String, Map<String, Integer>> items = new HashMap<>();

    public void save(String userId, String productId, int quantity) {
        get(userId).put(productId, quantity);
    }

    public Map<String, Integer> get(String userId) {
        items.putIfAbsent(userId, new HashMap<>());
        return items.get(userId);
    }

    public void delete(String userId, String productId) {
        get(userId).remove(productId);
    }

}