package com.grid.webdevelopment.controller;

import com.grid.webdevelopment.model.Product;
import com.grid.webdevelopment.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product/")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("get")
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("save")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok().build();
    }
}
