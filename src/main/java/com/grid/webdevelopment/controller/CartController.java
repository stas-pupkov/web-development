package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.Cart;
import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartView;
import com.grid.webdevelopment.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/cart/")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("add/{userId}")
    public ResponseEntity<Map<String, Integer>> addToCart(@PathVariable String userId,
                                                          @RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartService.addToCart(userId, cartItem));
    }

    @GetMapping("get/{userId}")
    public ResponseEntity<List<CartView>> display(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.displayItemsInCart(userId));
    }

    @PostMapping("delete/{userId}")
    public ResponseEntity<Map<String, Integer>> delete(@PathVariable String userId,
                                                       @RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartService.deleteFromCart(userId, cartItem));
    }

    @PostMapping("modify/{userId}")
    public ResponseEntity<Map<String, Integer>> modify(@PathVariable String userId,
                                                       @RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartService.modifyItemInCart(userId, cartItem));
    }

    @PostMapping("checkout/{userId}")
    public ResponseEntity<Cart> checkout(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.checkout(userId));
    }

    @GetMapping("orders/{userId}")
    public ResponseEntity<Set<Cart>> showOrders(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.showOrders(userId));
    }

}
