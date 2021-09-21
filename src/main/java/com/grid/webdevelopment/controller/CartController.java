package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartOrder;
import com.grid.webdevelopment.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<CartOrder>> display(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.displayCart(userId));
    }

    @PostMapping("delete/{userId}")
    public ResponseEntity<Map<String, Integer>> delete(@PathVariable String userId,
                                                       @RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartService.deleteItem(userId, cartItem));
    }

    @PostMapping("modify/{userId}")
    public ResponseEntity<Map<String, Integer>> modify(@PathVariable String userId,
                                                       @RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartService.modifyItemInCart(userId, cartItem));
    }

}
