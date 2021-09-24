package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductRepository {

    private final DefaultProducts products;

    public void save(Product product) {
        products.getProducts().put(product.getId(), product);
    }

    public Optional<Product> findById(String productId) {
        return products.getProducts().values().stream().filter(product -> product.getId().equals(productId)).findFirst();
    }

    public List<Product> findAll() {
        return products.getProducts().entrySet().stream().map(item -> item.getValue()).collect(Collectors.toList());
    }

}
