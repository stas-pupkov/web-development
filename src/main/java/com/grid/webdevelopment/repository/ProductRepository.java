package com.grid.webdevelopment.repository;

import com.grid.webdevelopment.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductRepository {

    private static HashMap<String, Product> items = new HashMap<>();

    public void save(Product product) {
        items.put(product.getId(), product);
    }

    public Product get(String id) {
        return items.get(id);
    }

    public List<Product> getAll() {
        return items.entrySet().stream().map(item -> item.getValue()).collect(Collectors.toList());
    }

}
