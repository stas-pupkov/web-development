package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.model.Cart;
import com.grid.webdevelopment.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class CartItemsRepository {

    private final UserService userService;

    public void save(String userId, String productId, int quantity) {
        Cart userCart = userService.getUserById(userId).getCart();
        userCart.getItems().put(productId, quantity);
        userService.getUserById(userId).setCart(userCart);
    }

    public Cart get(String userId) {
        return userService.getUserById(userId).getCart();
    }

    public void delete(String userId, String productId) {
        Cart userCart = userService.getUserById(userId).getCart();
        userCart.getItems().remove(productId);
        userService.getUserById(userId).setCart(userCart);
    }

}