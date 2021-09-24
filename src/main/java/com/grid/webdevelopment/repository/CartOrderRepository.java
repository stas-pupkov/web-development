package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.model.Cart;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@Service
public class CartOrderRepository {

    private final Map<String, Cart> orders;

    public void saveOrder(Cart cart) {
        orders.put(cart.getOrderId(), cart);
    }

}
