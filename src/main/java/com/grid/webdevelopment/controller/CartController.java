package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.CartItem;
import com.grid.webdevelopment.model.CartShow;
import com.grid.webdevelopment.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart/")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("add")
    public ResponseEntity<String> addToCart(@RequestBody CartItem cartItem) {
        return ResponseEntity.ok(cartService.addToCart(cartItem));
    }

    @GetMapping("get")
    public ResponseEntity<List<CartShow>> display() {
        return ResponseEntity.ok(cartService.displayCart());
    }

    @PostMapping("delete")
    public ResponseEntity<String> delete(@RequestBody CartItem cartItem) {
        return  ResponseEntity.ok(cartService.deleteItem(cartItem));
    }

    @PostMapping("modify")
    public ResponseEntity<String> modify(@RequestBody CartItem cartItem) {
        return  ResponseEntity.ok(cartService.modifyItemInCart(cartItem));
    }

}
